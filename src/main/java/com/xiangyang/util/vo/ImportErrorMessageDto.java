package com.xiangyang.util.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: xiangyang
 * @date:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorMessageDto implements Serializable {
    private static final long serialVersionUID = 3619867617973613677L;
    /**行*/
    private Integer row;
    /**错误消息*/
    private String message;
}
