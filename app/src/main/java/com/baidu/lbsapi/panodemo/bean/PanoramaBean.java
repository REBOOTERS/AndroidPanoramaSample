package com.baidu.lbsapi.panodemo.bean;

import java.util.List;

/**
 * Created by co-mall on 2016/8/31.
 */
public class PanoramaBean {

    /**
     * Admission : GS(2015)2210
     * Date : 20160125
     * DeviceHeight : 2.32
     * FileTag : pano_cfg
     * Heading : 4.78
     * ID : 09002200011601251135086222K
     * ImgLayer : [{"BlockX":2,"BlockY":1,"ImgFormat":"jpg","ImgLevel":1},{"BlockX":4,"BlockY":2,"ImgFormat":"jpg","ImgLevel":2},{"BlockX":8,"BlockY":4,"ImgFormat":"jpg","ImgLevel":3},{"BlockX":16,"BlockY":8,"ImgFormat":"jpg","ImgLevel":4}]
     * LayerCount : 4
     * Links : [{"CPointX":1295771406,"CPointY":483289453,"DIR":177,"PID":"09002200011601251135064362K","RID":"60910a-aa1e-ab69-ac1a-94c658","Type":"street","X":1295771457,"Y":483288360}]
     * Mode : day
     * MoveDir : 4.776
     * NorthDir : 265.22
     * Obsolete : 0
     * Pitch : -0.245
     * PoiDir : null
     * Provider : 9
     * Rname : 旧鼓楼外大街
     * Roads : [{"ID":"575750-8882-e57f-532f-473358","IsCurrent":1,"Name":"旧鼓楼外大街","Panos":[{"DIR":178,"Order":0,"PID":"0100220000130809125921503J2","Type":"street","X":1295770947,"Y":483291330},{"DIR":0,"Order":1,"PID":"09002200011601251135086222K","Type":"street","X":1295771476,"Y":483289392}],"Width":550},{"ID":"60910a-aa1e-ab69-ac1a-94c658","IsCurrent":0,"Name":"旧鼓楼外大街","Width":550}]
     * Roll : 0.264
     * SwitchID : []
     * Time : 201601
     * TimeLine : [{"ID":"09002200011601251135086222K","IsCurrent":1,"Time":"day","TimeDir":"","TimeLine":"201601","Year":"2016"},{"ID":"0100220000130809125918584J2","IsCurrent":0,"Time":"day","TimeDir":"obsolete","TimeLine":"201308","Year":"2013"}]
     * Type : street
     * UserID :
     * Version : 0
     * Z : 39.36
     * format_v : 0
     * panoinfo : null
     * plane :
     * procdate : 20160621
     * X : 12957714
     * RX : 12957717
     * Y : 4832893
     * RY : 4832894
     */

    private String Admission;
    private String Date;
    private double DeviceHeight;
    private String FileTag;
    private double Heading;
    private String ID;
    private int LayerCount;
    private String Mode;
    private double MoveDir;
    private double NorthDir;
    private int Obsolete;
    private double Pitch;
    private Object PoiDir;
    private int Provider;
    private String Rname;
    private double Roll;
    private String Time;
    private String Type;
    private String UserID;
    private String Version;
    private double Z;
    private String format_v;
    private Object panoinfo;
    private String plane;
    private String procdate;
    private int X;
    private int RX;
    private int Y;
    private int RY;
    /**
     * BlockX : 2
     * BlockY : 1
     * ImgFormat : jpg
     * ImgLevel : 1
     */

    private List<ImgLayerBean> ImgLayer;
    /**
     * CPointX : 1295771406
     * CPointY : 483289453
     * DIR : 177
     * PID : 09002200011601251135064362K
     * RID : 60910a-aa1e-ab69-ac1a-94c658
     * Type : street
     * X : 1295771457
     * Y : 483288360
     */

    private List<LinksBean> Links;
    /**
     * ID : 575750-8882-e57f-532f-473358
     * IsCurrent : 1
     * Name : 旧鼓楼外大街
     * Panos : [{"DIR":178,"Order":0,"PID":"0100220000130809125921503J2","Type":"street","X":1295770947,"Y":483291330},{"DIR":0,"Order":1,"PID":"09002200011601251135086222K","Type":"street","X":1295771476,"Y":483289392}]
     * Width : 550
     */

    private List<RoadsBean> Roads;
    private List<?> SwitchID;
    /**
     * ID : 09002200011601251135086222K
     * IsCurrent : 1
     * Time : day
     * TimeDir :
     * TimeLine : 201601
     * Year : 2016
     */

    private List<TimeLineBean> TimeLine;

    public String getAdmission() {
        return Admission;
    }

    public void setAdmission(String Admission) {
        this.Admission = Admission;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public double getDeviceHeight() {
        return DeviceHeight;
    }

    public void setDeviceHeight(double DeviceHeight) {
        this.DeviceHeight = DeviceHeight;
    }

    public String getFileTag() {
        return FileTag;
    }

    public void setFileTag(String FileTag) {
        this.FileTag = FileTag;
    }

    public double getHeading() {
        return Heading;
    }

    public void setHeading(double Heading) {
        this.Heading = Heading;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getLayerCount() {
        return LayerCount;
    }

    public void setLayerCount(int LayerCount) {
        this.LayerCount = LayerCount;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String Mode) {
        this.Mode = Mode;
    }

    public double getMoveDir() {
        return MoveDir;
    }

    public void setMoveDir(double MoveDir) {
        this.MoveDir = MoveDir;
    }

    public double getNorthDir() {
        return NorthDir;
    }

    public void setNorthDir(double NorthDir) {
        this.NorthDir = NorthDir;
    }

    public int getObsolete() {
        return Obsolete;
    }

    public void setObsolete(int Obsolete) {
        this.Obsolete = Obsolete;
    }

    public double getPitch() {
        return Pitch;
    }

    public void setPitch(double Pitch) {
        this.Pitch = Pitch;
    }

    public Object getPoiDir() {
        return PoiDir;
    }

    public void setPoiDir(Object PoiDir) {
        this.PoiDir = PoiDir;
    }

    public int getProvider() {
        return Provider;
    }

    public void setProvider(int Provider) {
        this.Provider = Provider;
    }

    public String getRname() {
        return Rname;
    }

    public void setRname(String Rname) {
        this.Rname = Rname;
    }

    public double getRoll() {
        return Roll;
    }

    public void setRoll(double Roll) {
        this.Roll = Roll;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double Z) {
        this.Z = Z;
    }

    public String getFormat_v() {
        return format_v;
    }

    public void setFormat_v(String format_v) {
        this.format_v = format_v;
    }

    public Object getPanoinfo() {
        return panoinfo;
    }

    public void setPanoinfo(Object panoinfo) {
        this.panoinfo = panoinfo;
    }

    public String getPlane() {
        return plane;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public String getProcdate() {
        return procdate;
    }

    public void setProcdate(String procdate) {
        this.procdate = procdate;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getRX() {
        return RX;
    }

    public void setRX(int RX) {
        this.RX = RX;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public int getRY() {
        return RY;
    }

    public void setRY(int RY) {
        this.RY = RY;
    }

    public List<ImgLayerBean> getImgLayer() {
        return ImgLayer;
    }

    public void setImgLayer(List<ImgLayerBean> ImgLayer) {
        this.ImgLayer = ImgLayer;
    }

    public List<LinksBean> getLinks() {
        return Links;
    }

    public void setLinks(List<LinksBean> Links) {
        this.Links = Links;
    }

    public List<RoadsBean> getRoads() {
        return Roads;
    }

    public void setRoads(List<RoadsBean> Roads) {
        this.Roads = Roads;
    }

    public List<?> getSwitchID() {
        return SwitchID;
    }

    public void setSwitchID(List<?> SwitchID) {
        this.SwitchID = SwitchID;
    }

    public List<TimeLineBean> getTimeLine() {
        return TimeLine;
    }

    public void setTimeLine(List<TimeLineBean> TimeLine) {
        this.TimeLine = TimeLine;
    }

    public static class ImgLayerBean {
        private int BlockX;
        private int BlockY;
        private String ImgFormat;
        private int ImgLevel;

        public int getBlockX() {
            return BlockX;
        }

        public void setBlockX(int BlockX) {
            this.BlockX = BlockX;
        }

        public int getBlockY() {
            return BlockY;
        }

        public void setBlockY(int BlockY) {
            this.BlockY = BlockY;
        }

        public String getImgFormat() {
            return ImgFormat;
        }

        public void setImgFormat(String ImgFormat) {
            this.ImgFormat = ImgFormat;
        }

        public int getImgLevel() {
            return ImgLevel;
        }

        public void setImgLevel(int ImgLevel) {
            this.ImgLevel = ImgLevel;
        }
    }

    public static class LinksBean {
        private int CPointX;
        private int CPointY;
        private int DIR;
        private String PID;
        private String RID;
        private String Type;
        private double X;
        private double Y;

        public int getCPointX() {
            return CPointX;
        }

        public void setCPointX(int CPointX) {
            this.CPointX = CPointX;
        }

        public int getCPointY() {
            return CPointY;
        }

        public void setCPointY(int CPointY) {
            this.CPointY = CPointY;
        }

        public int getDIR() {
            return DIR;
        }

        public void setDIR(int DIR) {
            this.DIR = DIR;
        }

        public String getPID() {
            return PID;
        }

        public void setPID(String PID) {
            this.PID = PID;
        }

        public String getRID() {
            return RID;
        }

        public void setRID(String RID) {
            this.RID = RID;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public double getX() {
            return X;
        }

        public void setX(double X) {
            this.X = X;
        }

        public double getY() {
            return Y;
        }

        public void setY(double Y) {
            this.Y = Y;
        }
    }

    public static class RoadsBean {
        private String ID;
        private int IsCurrent;
        private String Name;
        private int Width;
        /**
         * DIR : 178
         * Order : 0
         * PID : 0100220000130809125921503J2
         * Type : street
         * X : 1295770947
         * Y : 483291330
         */

        private List<PanosBean> Panos;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public int getIsCurrent() {
            return IsCurrent;
        }

        public void setIsCurrent(int IsCurrent) {
            this.IsCurrent = IsCurrent;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getWidth() {
            return Width;
        }

        public void setWidth(int Width) {
            this.Width = Width;
        }

        public List<PanosBean> getPanos() {
            return Panos;
        }

        public void setPanos(List<PanosBean> Panos) {
            this.Panos = Panos;
        }

        public static class PanosBean {
            private int DIR;
            private int Order;
            private String PID;
            private String Type;
            private int X;
            private int Y;

            public int getDIR() {
                return DIR;
            }

            public void setDIR(int DIR) {
                this.DIR = DIR;
            }

            public int getOrder() {
                return Order;
            }

            public void setOrder(int Order) {
                this.Order = Order;
            }

            public String getPID() {
                return PID;
            }

            public void setPID(String PID) {
                this.PID = PID;
            }

            public String getType() {
                return Type;
            }

            public void setType(String Type) {
                this.Type = Type;
            }

            public int getX() {
                return X;
            }

            public void setX(int X) {
                this.X = X;
            }

            public int getY() {
                return Y;
            }

            public void setY(int Y) {
                this.Y = Y;
            }
        }
    }

    public static class TimeLineBean {
        private String ID;
        private int IsCurrent;
        private String Time;
        private String TimeDir;
        private String TimeLine;
        private String Year;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public int getIsCurrent() {
            return IsCurrent;
        }

        public void setIsCurrent(int IsCurrent) {
            this.IsCurrent = IsCurrent;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getTimeDir() {
            return TimeDir;
        }

        public void setTimeDir(String TimeDir) {
            this.TimeDir = TimeDir;
        }

        public String getTimeLine() {
            return TimeLine;
        }

        public void setTimeLine(String TimeLine) {
            this.TimeLine = TimeLine;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String Year) {
            this.Year = Year;
        }
    }
}
