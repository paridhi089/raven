package dev.paridhi.raven.others;

public class Helper {

    public String getInitals(String displayName)
    {
        String[] names = displayName.split("\\s+");
        String initals;
        int lenArray=names.length;

        if(lenArray>1) {
            String fname = names[0];
            String lname = names[lenArray - 1];
            initals = fname.substring(0, 1) + lname.substring(0, 1);
            return initals.toUpperCase();
        }
        else
        {
            String fname = names[0];
            return fname.substring(0, 1);
        }
    }
}
