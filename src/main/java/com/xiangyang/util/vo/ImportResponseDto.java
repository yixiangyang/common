package com.xiangyang.util.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: xiangyang
 * @date:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportResponseDto implements Serializable {
    private static final long serialVersionUID = 1229874274387392305L;

    private List dataList;
    /**错误消息*/
    private List<ImportErrorMessageDto> errorList;

}
