package com.hgzp.advertising.pagemodel.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@Accessors(chain = true)
public class PreOrderExtVO {
    private String mediatypekey;
    private String mediatypename;
    private String mediaid;
    private String medianame;
    private String foldid;
    private String foldname;
}
