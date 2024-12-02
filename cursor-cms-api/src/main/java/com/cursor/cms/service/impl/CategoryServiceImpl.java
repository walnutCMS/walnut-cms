package com.cursor.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cursor.cms.common.exception.BusinessException;
import com.cursor.cms.dto.CategoryDTO;
import com.cursor.cms.entity.Category;
import com.cursor.cms.mapper.CategoryMapper;
import com.cursor.cms.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<CategoryDTO> getCategoryList() {
        // 获取所有未删除的栏目
        List<Category> categories = list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort));

        // 转换为DTO并构建树形结构
        return buildTree(categories);
    }

    @Override
    public CategoryDTO getCategoryById(String id) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("栏目不存在");
        }
        return convertToDTO(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveCategory(CategoryDTO dto) {
        // 检查父栏目是否存在
        if (StringUtils.hasText(dto.getParentId())) {
            Category parent = getById(dto.getParentId());
            if (parent == null) {
                throw new BusinessException("父栏目不存在");
            }
        }

        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        
        // 设置层级和路径
        if (StringUtils.hasText(dto.getParentId())) {
            Category parent = getById(dto.getParentId());
            category.setLevel(parent.getLevel() + 1);
            category.setPath(parent.getPath() + "/" + parent.getId());
        } else {
            category.setLevel(1);
            category.setPath("");
        }

        // 保存栏目
        save(category);
        
        // 更新路径
        category.setPath(category.getPath() + "/" + category.getId());
        updateById(category);

        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(String id, CategoryDTO dto) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("栏目不存在");
        }

        // 检查是否修改了父栏目
        if (!Objects.equals(category.getParentId(), dto.getParentId())) {
            // 检查新的父栏目是否存在
            if (StringUtils.hasText(dto.getParentId())) {
                Category parent = getById(dto.getParentId());
                if (parent == null) {
                    throw new BusinessException("父栏目不存在");
                }
                // 检查是否将栏目移动到了其子栏目下
                if (isChild(id, dto.getParentId())) {
                    throw new BusinessException("不能将栏目移动到其子栏目下");
                }
                // 更新层级和路径
                category.setLevel(parent.getLevel() + 1);
                category.setPath(parent.getPath() + "/" + parent.getId() + "/" + id);
            } else {
                category.setLevel(1);
                category.setPath("/" + id);
            }
        }

        BeanUtils.copyProperties(dto, category);
        category.setId(id);  // 确保ID不被覆盖
        updateById(category);

        // 如果修改了父栏目，需要更新所有子栏目的层级和路径
        if (!Objects.equals(category.getParentId(), dto.getParentId())) {
            updateChildrenLevelAndPath(category);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(String id) {
        // 检查是否存在子栏目
        List<Category> children = list(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, id));
        if (!children.isEmpty()) {
            throw new BusinessException("存在子栏目，不能删除");
        }

        removeById(id);
    }

    @Override
    public List<CategoryDTO> getChildCategories(String parentId) {
        List<Category> children = list(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parentId)
                .orderByAsc(Category::getSort));
        return children.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getCategoryPath(String id) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("栏目不存在");
        }

        List<CategoryDTO> path = new ArrayList<>();
        String[] ids = category.getPath().split("/");
        for (String pathId : ids) {
            if (StringUtils.hasText(pathId)) {
                Category pathCategory = getById(pathId);
                if (pathCategory != null) {
                    path.add(convertToDTO(pathCategory));
                }
            }
        }
        path.add(convertToDTO(category));
        return path;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSort(String id, Integer sort) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("栏目不存在");
        }
        category.setSort(sort);
        updateById(category);
    }

    private List<CategoryDTO> buildTree(List<Category> categories) {
        Map<String, CategoryDTO> dtoMap = new HashMap<>();
        List<CategoryDTO> roots = new ArrayList<>();

        // 转换为DTO并建立映射关系
        for (Category category : categories) {
            CategoryDTO dto = convertToDTO(category);
            dtoMap.put(dto.getId(), dto);
        }

        // 构建树形结构
        for (CategoryDTO dto : dtoMap.values()) {
            if (!StringUtils.hasText(dto.getParentId())) {
                roots.add(dto);
            } else {
                CategoryDTO parent = dtoMap.get(dto.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(dto);
                }
            }
        }

        // 对每个节点的子节点进行排序
        for (CategoryDTO dto : dtoMap.values()) {
            if (dto.getChildren() != null) {
                dto.getChildren().sort(Comparator.comparing(CategoryDTO::getSort));
            }
        }

        // 对根节点进行排序
        roots.sort(Comparator.comparing(CategoryDTO::getSort));
        return roots;
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }

    private boolean isChild(String parentId, String childId) {
        if (!StringUtils.hasText(childId)) {
            return false;
        }
        Category child = getById(childId);
        if (child == null) {
            return false;
        }
        return child.getPath().contains("/" + parentId + "/");
    }

    private void updateChildrenLevelAndPath(Category parent) {
        List<Category> children = list(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parent.getId()));
        for (Category child : children) {
            child.setLevel(parent.getLevel() + 1);
            child.setPath(parent.getPath() + "/" + child.getId());
            updateById(child);
            updateChildrenLevelAndPath(child);
        }
    }
}