module common {
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.annotation;
    requires com.google.common;
    requires java.sql;
    requires com.google.gson;

    exports com.dtu.common;
    exports com.dtu.common.observer;
    exports com.dtu.common.model;
    exports com.dtu.common.model.fileaccess;
    exports com.dtu.common.controller;
    exports com.dtu.common.model.fieldTypes;
}