package com.sun.syncupload.model;

import org.gradle.api.NamedDomainObjectFactory;

/**
 * 模板代码：提供模型
 * gradle 会自动查找 NamedDomainObjectFactory 的继承者
 */
class UploadModuleFactory implements NamedDomainObjectFactory<UploadModule> {
    @Override
    public UploadModule create(String name) {
        return new UploadModule(name);
    }
}
