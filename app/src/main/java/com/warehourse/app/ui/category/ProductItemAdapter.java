package com.warehourse.app.ui.category;

import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.ui.base.ProductItemHolder;

/**
 * Title: ProductItemAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:11/05/2017  17:03
 *
 * @author johnzheng
 * @version 1.0
 */

 class ProductItemAdapter extends BaseQuickAdapter<ProductEntity, ProductItemHolder> {

    public ProductItemAdapter() {
        super(R.layout.item_category_product_layout);
    }


    @Override
    protected void convert(ProductItemHolder holder, ProductEntity entity) {
        holder.bindData(entity);
    }


}
