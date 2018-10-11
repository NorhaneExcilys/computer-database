package ui;

import exception.UnknowYesOrNoException;

public enum YesOrNoEnum {

	YES("yes"), NO("no");
	
	final private String value; 
	 
	YesOrNoEnum(String value) { 
		this.value = value; 
    } 
 
    public String getValue() { 
         return value; 
    }
    
    public static YesOrNoEnum stringToYesOrNoEnum (String userInput) throws UnknowYesOrNoException { 
        for (YesOrNoEnum yesOrNoEnum : values()) { 
             if (yesOrNoEnum.getValue().equalsIgnoreCase(userInput)) { 
                  return yesOrNoEnum; 
             }
        }
        throw new UnknowYesOrNoException(); 
    }
}
