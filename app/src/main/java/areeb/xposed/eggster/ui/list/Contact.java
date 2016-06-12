package areeb.xposed.eggster.ui.list;

import areeb.xposed.eggster.R;

public class Contact {
    public String url, description;
    public int resId;

    public Contact(String url, String description, int resId){
        this.url = url;
        this.description = description;
        this.resId = resId;
    }

    public static Contact[] getContacts(){
        Contact[] contacts = {
                new Contact("https://github.com/iamareebjamal/eggster_xposed",
                        "Source Code", R.drawable.ic_git),                      // Github
                new Contact("http://forum.xda-developers.com/xposed/modules/xposed-eggster-1-2-replace-easter-eggs-t2758583",
                        "XDA", R.drawable.ic_xda),                              // XDA
                new Contact("https://plus.google.com/u/0/+areebjamaliam",
                        "Google+", R.drawable.ic_gplus),                        // G+
                new Contact("https://fb.me/iamareebjamal",
                        "Facebook", R.drawable.ic_fb),                          // Facebook
                new Contact("mailto:jamal.areeb@gmail.com",
                        "Email", R.drawable.ic_gmail),                          // Gmail
        };

        return contacts;
    }
}
