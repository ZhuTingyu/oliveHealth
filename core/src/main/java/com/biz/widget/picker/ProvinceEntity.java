package com.biz.widget.picker;

import java.util.ArrayList;

/**
 * Created by johnzheng on 3/23/16.
 */
public class ProvinceEntity {


    /**
     * id : 289
     * name : 上海
     * cities : [{"id":289,"name":"上海","districts":[{"id":1422,"name":"宝山区","level":4},{"id":1423,"name":"闵行区","level":4},{"id":1514,"name":"金山区","level":4},{"id":1839,"name":"杨浦区","level":4},{"id":1840,"name":"普陀区","level":4},{"id":1841,"name":"松江区","level":4},{"id":1842,"name":"卢湾区","level":4},{"id":2183,"name":"浦东新区","level":4},{"id":2184,"name":"长宁区","level":4},{"id":2294,"name":"奉贤区","level":4},{"id":2466,"name":"虹口区","level":4},{"id":2467,"name":"徐汇区","level":4},{"id":2694,"name":"闸北区","level":4},{"id":2793,"name":"嘉定区","level":4},{"id":2826,"name":"静安区","level":4},{"id":2892,"name":"青浦区","level":4},{"id":2896,"name":"黄浦区","level":4},{"id":2908,"name":"崇明县","level":4}],"level":3}]
     * level : 2
     */

    private int id;
    private String name;
    private int level;
    /**
     * id : 289
     * name : 上海
     * districts : [{"id":1422,"name":"宝山区","level":4},{"id":1423,"name":"闵行区","level":4},{"id":1514,"name":"金山区","level":4},{"id":1839,"name":"杨浦区","level":4},{"id":1840,"name":"普陀区","level":4},{"id":1841,"name":"松江区","level":4},{"id":1842,"name":"卢湾区","level":4},{"id":2183,"name":"浦东新区","level":4},{"id":2184,"name":"长宁区","level":4},{"id":2294,"name":"奉贤区","level":4},{"id":2466,"name":"虹口区","level":4},{"id":2467,"name":"徐汇区","level":4},{"id":2694,"name":"闸北区","level":4},{"id":2793,"name":"嘉定区","level":4},{"id":2826,"name":"静安区","level":4},{"id":2892,"name":"青浦区","level":4},{"id":2896,"name":"黄浦区","level":4},{"id":2908,"name":"崇明县","level":4}]
     * level : 3
     */

    private ArrayList<City> cities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public static class City {
        private int id;
        private String name;
        private int level;
        /**
         * id : 1422
         * name : 宝山区
         * level : 4
         */

        private ArrayList<Districty> districts;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public ArrayList<Districty> getDistricts() {
            return districts;
        }

        public void setDistricts(ArrayList<Districty> districts) {
            this.districts = districts;
        }

        public static class Districty {
            private int id;
            private String name;
            private int level;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }
        }
    }
}
