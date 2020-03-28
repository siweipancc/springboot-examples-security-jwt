package cn.pancc.springboot.examples.security.jwt;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用前端返回数据包装
 *
 * @author pancc
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -3601247677280531909L;
    /**
     * 传递状态码，默认 200
     */
    private int status = 200;

    /**
     * 包装后端业务输出数据
     */
    private Object data="";


    /**
     * 发生错误时可传递错误信息 ，默认值 "success" 即不发生错错误
     */
    private String message = "success";

    /**
     * 打印时间戳，不允许修改
     */
    private Date timestamp = new Date();

    @SuppressWarnings("unused")
    private void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
