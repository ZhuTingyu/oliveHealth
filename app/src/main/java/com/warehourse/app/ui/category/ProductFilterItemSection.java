package com.warehourse.app.ui.category;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.warehourse.app.model.entity.ProductFilterItemEntity;

/**
 * Title: ProductFilterItemSection
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:24/05/2017  15:58
 *
 * @author johnzheng
 * @version 1.0
 */

public class ProductFilterItemSection extends SectionEntity<ProductFilterItemEntity> {

    public ProductFilterItemSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ProductFilterItemSection(ProductFilterItemEntity entity){
        super(entity);
    }
}
