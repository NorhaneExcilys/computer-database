package ui;

import exception.UnknowMenuException;

public enum MenuEnum {
	
	GET_COMPUTERS("1"), GET_COMPANIES("2"), GET_COMPUTER_BY_ID("3"), ADD_COMPUTER("4"), UPDATE_COMPUTER("5"), DELETE_COMPUTER("6"), DELETE_COMPANY("7"), QUIT("8"), HELP("help");

    final private String value; 
 
    MenuEnum(String value) { 
         this.value = value; 
    } 
 
    public String getValue() { 
         return value; 
    }
    
    public static MenuEnum stringToMenuEnum (String value) throws UnknowMenuException { 
        for (MenuEnum menuEnum : values()) { 
             if (menuEnum.getValue().equalsIgnoreCase(value)) { 
                  return menuEnum; 
             }
        }
        throw new UnknowMenuException(); 
    }
}