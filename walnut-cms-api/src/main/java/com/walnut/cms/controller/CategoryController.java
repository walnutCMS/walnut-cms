package com.walnut.cms.controller;

import com.walnut.cms.common.Result;
import com.walnut.cms.dto.CategoryDTO;
import com.walnut.cms.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Tag(name = "栏目管理")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "获取栏目列表")
    public Result<List<CategoryDTO>> list() {
        return Result.success(categoryService.getCategoryList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取栏目详情")
    public Result<CategoryDTO> getById(@PathVariable String id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    @PostMapping
    @Operation(summary = "创建栏目")
    public Result<Map<String, String>> save(@RequestBody CategoryDTO dto) {
        String id = categoryService.saveCategory(dto);
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        return Result.success(data);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新栏目")
    public Result<Void> update(@PathVariable String id, @RequestBody CategoryDTO dto) {
        categoryService.updateCategory(id, dto);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除栏目")
    public Result<Void> delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return Result.success(null);
    }

    @GetMapping("/children/{parentId}")
    @Operation(summary = "获取子栏目")
    public Result<List<CategoryDTO>> getChildren(@PathVariable String parentId) {
        return Result.success(categoryService.getChildCategories(parentId));
    }

    @GetMapping("/path/{id}")
    @Operation(summary = "获取栏目路径")
    public Result<List<CategoryDTO>> getPath(@PathVariable String id) {
        return Result.success(categoryService.getCategoryPath(id));
    }

    @PutMapping("/{id}/sort")
    @Operation(summary = "更新栏目排序")
    public Result<Void> updateSort(@PathVariable String id, @RequestParam Integer sort) {
        categoryService.updateSort(id, sort);
        return Result.success(null);
    }
}