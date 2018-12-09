package tom.pongzai.com.testaccount;

import android.os.Parcel;
import android.os.Parcelable;

//public class UserModel {
    public class UserModel implements Parcelable {

    private String uavatarString, uidString, umessageString, unameString;

    public UserModel() {
    }

    public UserModel(String uavatarString, String uidString, String umessageString, String unameString) {
        this.uavatarString = uavatarString;
        this.uidString = uidString;
        this.umessageString = umessageString;
        this.unameString = unameString;
    }

    protected UserModel(Parcel in) {
        uavatarString = in.readString();
        uidString = in.readString();
        umessageString = in.readString();
        unameString = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUavatarString() {
        return uavatarString;
    }

    public void setUavatarString(String uavatarString) {
        this.uavatarString = uavatarString;
    }

    public String getUidString() {
        return uidString;
    }

    public void setUidString(String uidString) {
        this.uidString = uidString;
    }

    public String getUmessageString() {
        return umessageString;
    }

    public void setUmessageString(String umessageString) {
        this.umessageString = umessageString;
    }

    public String getUnameString() {
        return unameString;
    }

    public void setUnameString(String unameString) {
        this.unameString = unameString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uavatarString);
        dest.writeString(uidString);
        dest.writeString(umessageString);
        dest.writeString(unameString);
    }
}
