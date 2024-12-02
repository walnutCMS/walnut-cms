package com.walnut.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.walnut.cms.dto.CategoryDTO;
import com.walnut.cms.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 获取栏目列表（树形结构）
     * @return 栏目列表
     */
    List<CategoryDTO> getCategoryList();

    /**
     * 获取栏目详情
     * @param id 栏目ID
     * @return 栏目信息
     */
    CategoryDTO getCategoryById(String id);

    /**
     * 保存栏目
     * @param dto 栏目信息
     * @return 栏目ID
     */
    String saveCategory(CategoryDTO dto);

    /**
     * 更新栏目
     * @param id 栏目ID
     * @param dto 栏目信息
     */
    void updateCategory(String id, CategoryDTO dto);

    /**
     * 删除栏目
     * @param id 栏目ID
     */
    void deleteCategory(String id);

    /**
     * 获取子栏目列表
     * @param parentId 父栏目ID
     * @return 子栏目列表
     */
    List<CategoryDTO> getChildCategories(String parentId);

    /**
     * 获取栏目路径
     * @param id 栏目ID
     * @return 栏目路径
     */
    List<CategoryDTO> getCategoryPath(String id);

    /**
     * 更新栏目排序
     * @param id 栏目ID
     * @param sort 排序值
     */
    void updateSort(String id, Integer sort);
}