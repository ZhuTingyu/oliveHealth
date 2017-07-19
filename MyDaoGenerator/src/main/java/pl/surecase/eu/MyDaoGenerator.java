package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
//        Schema schema = new Schema( 1, "com.warehouse.dao");
//        schema.setDefaultJavaPackageDao("com.warehouse.dao");
//        initUserBean(schema);
//        new DaoGenerator().generateAll(schema, args[0]);
        Schema schema = new Schema(5, "com.warehouse.dao");
        initConfigBean(schema);
        new DaoGenerator().generateAll(schema, args[0]);
    }
    private static void initConfigBean(Schema schema) {
        Entity configBean = schema.addEntity("ConfigBean");
        configBean.setTableName("ConfigDB");
        configBean.addStringProperty("id").primaryKey().index();
        configBean.addStringProperty("type").index();
        configBean.addStringProperty("cache");
        configBean.addStringProperty("userId");
        configBean.addStringProperty("key");
        configBean.addLongProperty("ts");

        Entity avgBean=schema.addEntity("AvgBean");
        avgBean.setTableName("AvgDB");
        avgBean.addLongProperty("id").primaryKey().index();
        avgBean.addStringProperty("imgUrl");
        avgBean.addStringProperty("clickUrl");
        avgBean.addLongProperty("startTime");
        avgBean.addLongProperty("endTime");
        avgBean.addIntProperty("priority");
        avgBean.addLongProperty("stopTime");

        Entity orderCart=schema.addEntity("OrderCartBean");
        orderCart.addLongProperty("id").primaryKey().index();
        orderCart.addLongProperty("productId");
        orderCart.addLongProperty("userId");
        orderCart.addIntProperty("count");
        orderCart.addLongProperty("ts");

        Entity message=schema.addEntity("MessageBean");
        message.addLongProperty("sysId").primaryKey().index();
        message.addLongProperty("id");
        message.addStringProperty("userId");
        message.addStringProperty("title");
        message.addStringProperty("url");
        message.addStringProperty("content");
        message.addLongProperty("createTime");
        message.addBooleanProperty("isRead");
    }
}
