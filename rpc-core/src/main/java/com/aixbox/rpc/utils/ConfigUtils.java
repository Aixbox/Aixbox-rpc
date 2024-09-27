package com.aixbox.rpc.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.yaml.YamlUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.Map;

/**
 * Description: 配置工具类
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 下午2:08
 */
public class ConfigUtils {

    /**
     * 加载配置对象
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static<T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置对象，支持区分环境
     * @param tClass
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }

        //按yml yaml properties顺序查找，若未找到，则使用默认值
        T config = null;

        Yaml yaml = new Yaml();
        //读取application.yml
        String ymlFile = configFileBuilder.toString() + ".yml";
        if (ResourceUtil.getResource(ymlFile) != null) {
            String ymlContent = ResourceUtil.readUtf8Str(ymlFile);
            Map<String, Object> load = yaml.load(ymlContent);
            return BeanUtil.toBean(load.get(prefix), tClass);
        }

        //读取yaml文件
        String yamlFile = configFileBuilder.toString() + ".yaml";
        if (ResourceUtil.getResource(yamlFile) != null) {
            String yamlContent = ResourceUtil.readUtf8Str(yamlFile);
            Map<String, Object> load = yaml.load(yamlContent);
            return BeanUtil.toBean(load.get(prefix), tClass);
        }


        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass, prefix);
    }

}
















