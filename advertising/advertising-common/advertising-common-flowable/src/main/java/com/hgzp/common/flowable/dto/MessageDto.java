package com.hgzp.common.flowable.dto;

import lombok.Data;

/**
 * 消息
 */
@Data
public class MessageDto extends PageDto{

    private Boolean readed;

    private Long id;

}
