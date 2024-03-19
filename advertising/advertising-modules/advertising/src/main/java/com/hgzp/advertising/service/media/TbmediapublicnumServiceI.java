package com.hgzp.advertising.service.media;

import com.hgzp.core.model.Tbmediapublicnum;
import com.hgzp.service.common.IMyService;

import java.util.Date;

/**
 * @author new wei
 * @date 2023/12/21 10:03
 */
public interface TbmediapublicnumServiceI extends IMyService<Tbmediapublicnum> {
    String getMediaPublishNO(Long mediaId, Date spublishDate);
}
