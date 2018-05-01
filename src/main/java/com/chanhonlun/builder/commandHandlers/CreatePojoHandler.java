package com.chanhonlun.builder.commandHandlers;

import com.chanhonlun.builder.models.TableColumn;
import com.chanhonlun.builder.models.TemplateColumn;
import com.chanhonlun.builder.utils.JDBCUtil;
import com.chanhonlun.builder.utils.StrUtil;
import com.chanhonlun.builder.utils.TemplateUtil;
import com.chanhonlun.command.handlers.Handler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePojoHandler implements Handler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(String... arguments) {

        String tableName = arguments[0];

        List<TableColumn> tableColumns = JDBCUtil.getTableColumns(tableName);

        logger.debug("table: {}, columns: {}", tableName, tableColumns);

        List<TemplateColumn> templateColumns = TemplateUtil.getColumnsForTemplate(tableColumns);

        logger.debug("templateColumns: {}", templateColumns);

        Map<String, Object> data = new HashMap<>();
        data.put("packageName", "com.chanhonlun.server.pojos");
        data.put("tableName", tableName);
        data.put("javaTableName", StrUtil.javaName(tableName, true));
        data.put("columns", templateColumns);

        logger.debug("map for template: {}", data);

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/pojo.twig");
        JtwigModel model = JtwigModel.newModel(data);
        template.render(model, System.out);

    }
}
