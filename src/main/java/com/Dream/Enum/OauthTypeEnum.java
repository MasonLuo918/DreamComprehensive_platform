package com.Dream.Enum;

public enum OauthTypeEnum {
    QQ(1,"QQ"), YIBAN(2,"YIBAN");

    private Integer id;

    private String text;

    OauthTypeEnum(Integer id, String text){
        this.id = id;
        this.text = text;
    }

    public static OauthTypeEnum getByID(Integer id){
        for(OauthTypeEnum type:OauthTypeEnum.values()){
            if(type.getId().equals(id)){
                return type;
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

