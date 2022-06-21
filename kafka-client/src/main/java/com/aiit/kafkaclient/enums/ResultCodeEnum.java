package com.aiit.kafkaclient.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举信息：错误code/message
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    // 成功代码
    SUCCESS("00000", "执行成功"),

    // 失败代码
    IDENTITY_VERIFICATION_FAILED("A0000", "token不合法"),
    QUERY_ERROR("A0001", "数据库查询不到数据"),
    ILLEGAL_PARAM("A0002", "参数校验失败"),
    DATABASE_DRIVER_ERROR("A0004", "数据库驱动加载失败"),
    DATABASE_CONNECT_ERROR("A0005","数据库连接失败"),
    NULL_PARAM("A0006", "参数为空"),
    SQL_SYNTAX_ERROR("A0007", "sql句法错误"),
    API_POST_ERROR("A0008", "接口调用失败"),
    RELEASE_ERROR("A0009","资源释放失败"),
    DATA_VIRTUALIZATION_POST_ERROR("A0010", "虚拟层接口调用失败"),
    FILE_MISS_HEADER("A0020", "输入文件不包含表头，无法获取字段信息"),
    FILE_NOT_FOUND_ERROR("A0021", "文件不存在"),
    FILE_IO_ERROR("A0022", "读取文件失败"),
    NO_TOKEN_IN_REQUEST("A0220", "请求头未包含token"),
    TOKEN_FORMAT_ERROR("A0221", "token格式错误"),
    IDENTITY_VERIFY_ERROR("A0222", "身份校验失败，请重新登录"),
    ACCESS_FORBIDDEN("B0001", "无权限访问");

    private String code;
    private String message;

}
