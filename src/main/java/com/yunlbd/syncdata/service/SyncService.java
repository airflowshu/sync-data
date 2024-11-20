package com.yunlbd.syncdata.service;

import com.yunlbd.syncdata.config.SyncProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SyncService {

    @Autowired
    @Qualifier("dataFromJdbcTemplate")
    private JdbcTemplate dataFromJdbcTemplate;

    @Autowired
    @Qualifier("dataTargetJdbcTemplate")
    private JdbcTemplate dataTargetJdbcTemplate;

    @Resource
    IMailService iMailService;

    @Resource
    SyncProperties syncProperties;

    public void syncData() {
        try {
            log.info("Start to sync data.....{}", System.currentTimeMillis());
            syncProperties.getTables().forEach(this::syncTable);
            if (syncProperties.getSchedule().sendmail) {
                iMailService.sendSimpleMail("657296200@qq.com", "dchy数据同步！", "已同步" + syncProperties.getTables().size() + "张表");
            }
        } catch (Exception e) {
            log.error("Sync data error:", e);
            if (syncProperties.getSchedule().sendmail) {
                iMailService.sendSimpleMail("657296200@qq.com", "dchy数据同步失败！", "邮件内容.....<br/>" + e.getMessage());
            }
        }
    }

    static final String ID_COLUMN_ROWID = "rowid";
    static final String ID_COLUMN_ID = "id";

    private void syncTable(String tableName) {
        List<Map<String, Object>> sourceData = dataFromJdbcTemplate.queryForList(
                "SELECT * FROM " + tableName
        );

        for (Map<String, Object> row : sourceData) {
            String idColumn = ID_COLUMN_ROWID;
            Object id = row.get(ID_COLUMN_ROWID);
            if (id == null) {
                id = row.get(ID_COLUMN_ID);
                idColumn = ID_COLUMN_ID;
            }
            Map<String, Object> existingData = null;
            try {
                existingData = dataTargetJdbcTemplate.queryForMap(
                        "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?", id
                );
            } catch (DataAccessException e) {
                // throw new RuntimeException(e);
            }

            if (existingData != null) {
                // Update existing record
                List<String> columnsList = new ArrayList<>();
                List<Object> updateParamsList = new ArrayList<>();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    if (!idColumn.equals(entry.getKey())) {
                        columnsList.add(entry.getKey());
                        updateParamsList.add(entry.getValue()); // 收集更新参数
                    }
                }
                String columns = String.join(", ", columnsList);
                Object[] updateParams = updateParamsList.toArray(); // 转换为数组
                Object[] allParams = ArrayUtils.addAll(updateParams, id); // 添加 id 到参数数组末尾

                String updateSql = "UPDATE " + tableName + " SET " +
                        columns.replaceAll(", ", " = ?, ") + " = ? WHERE " + idColumn + " = ?"; // 构建更新 SQL
                dataTargetJdbcTemplate.update(updateSql, allParams); // 执行更新操作
            } else {
                // Insert new record
                List<String> columns = new ArrayList<>();
                List<Object> insertParams = new ArrayList<>();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    if (!idColumn.equalsIgnoreCase(entry.getKey())) {
                        columns.add(entry.getKey());
                        insertParams.add(entry.getValue()); // 收集插入参数，除了id
                    }
                }
                insertParams.add(id); // 在最后添加id参数

                // 构建 INSERT SQL 语句
                String sqlColumns = String.join(", ", columns);
                String sqlPlaceholders = columns.stream().map(c -> "?").collect(Collectors.joining(", "));
                String insertSql = "INSERT INTO " + tableName + " (" + sqlColumns + ", " + idColumn + ") VALUES (" + sqlPlaceholders + ", ?)";

                // 执行插入操作，确保参数类型与数据库列类型匹配
                dataTargetJdbcTemplate.update(insertSql, insertParams.toArray());
            }
        }
        log.info("Sync table " + tableName + " finished. 已同步" + sourceData.size() + "条数据");
    }
}
