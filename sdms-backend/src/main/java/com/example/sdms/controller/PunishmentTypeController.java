package com.example.sdms.controller;
import com.example.sdms.common.ResultVO;
import com.example.sdms.entity.PunishmentType;
import com.example.sdms.service.PunishmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/punishment-type")
public class PunishmentTypeController {
    @Autowired
    private PunishmentTypeService service;

    @GetMapping("/list")
    public ResultVO<List<PunishmentType>> list() {
        return ResultVO.success(service.list());
    }

    @PostMapping("/save")
    public ResultVO<?> save(@RequestBody PunishmentType type) {
        service.saveOrUpdate(type);
        return ResultVO.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        boolean removed = service.removeById(id);
        if (removed) {
            return ResultVO.success(null);
        } else {
            return ResultVO.error("删除失败，记录不存在");
        }
    }
} 