//package com.java110.ext.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import com.java110.core.response.Result;
//import com.java110.ext.service.IDemoService;
//import com.java110.ext.pojo.DemoPo;
//
///**
// * 示例控制器
// */
//@RestController
//@RequestMapping("/api/demo")
//public class DemoController {
//
//    @Autowired
//    private IDemoService demoService;
//
//    /**
//     * 查询列表
//     */
//    @GetMapping("/list")
//    public Result list(DemoPo demoPo) {
//        return Result.ok(demoService.listDemo(demoPo));
//    }
//
//    /**
//     * 保存数据
//     */
//    @PostMapping("/save")
//    public Result save(@RequestBody DemoPo demoPo) {
//        return Result.ok(demoService.saveDemo(demoPo));
//    }
//
//    /**
//     * 更新数据
//     */
//    @PutMapping("/update")
//    public Result update(@RequestBody DemoPo demoPo) {
//        return Result.ok(demoService.updateDemo(demoPo));
//    }
//
//    /**
//     * 删除数据
//     */
//    @DeleteMapping("/delete")
//    public Result delete(@RequestBody DemoPo demoPo) {
//        return Result.ok(demoService.deleteDemo(demoPo));
//    }
//
//    /**
//     * 根据ID查询
//     */
//    @GetMapping("/get/{id}")
//    public Result get(@PathVariable("id") String id) {
//        return Result.ok(demoService.getDemoById(id));
//    }
//}
