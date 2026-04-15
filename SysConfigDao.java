package dao;

import java.util.List;

import bean.SysConfig;

public interface SysConfigDao {
    List<SysConfig> findAll();

    int update(SysConfig config);
}