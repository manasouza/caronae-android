package br.ufrj.caronae.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    private String name;
    private String profile;
    private String course;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String email;
    @SerializedName("car_owner")
    private boolean carOwner;
    @SerializedName("car_model")
    private String carModel;
    @SerializedName("car_color")
    private String carColor;
    @SerializedName("car_plate")
    private String carPlate;
    @SerializedName("id")
    private int dbId;
    @SerializedName("created_at")
    private String createdAt;
    private String location;
    @SerializedName("profile_pic_url")
    private String profilePicUrl;
    @SerializedName("face_id")
    private String faceId;
    @SerializedName("id_ufrj")
    private String idUfrj;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCarOwner() {
        return carOwner;
    }

    public void setCarOwner(boolean carOwner) {
        this.carOwner = carOwner;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getIdUfrj() {
        return idUfrj;
    }

    public void setIdUfrj(String idUfrj) {
        this.idUfrj = idUfrj;
    }

    public boolean sameFieldsState(User user) {
        if (isCarOwner() != user.isCarOwner()) return false;
        if (!getName().equals(user.getName())) return false;
        if (getProfile() != null ? !getProfile().equals(user.getProfile()) : user.getProfile() != null)
            return false;
        if (getCourse() != null ? !getCourse().equals(user.getCourse()) : user.getCourse() != null)
            return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(user.getPhoneNumber()) : user.getPhoneNumber() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null)
            return false;
        if (getLocation() != null ? !getLocation().equals(user.getLocation()) : user.getLocation() != null)
            return false;
        if (getCarModel() != null ? !getCarModel().equals(user.getCarModel()) : user.getCarModel() != null)
            return false;
        //noinspection SimplifiableIfStatement
        if (getCarColor() != null ? !getCarColor().equals(user.getCarColor()) : user.getCarColor() != null)
            return false;
        return !(getCarPlate() != null ? !getCarPlate().equals(user.getCarPlate()) : user.getCarPlate() != null);
    }

    public void setUser(User editedUser) {
        setName(editedUser.getName());
        setProfile(editedUser.getProfile());
        setCourse(editedUser.getCourse());
        setPhoneNumber(editedUser.getPhoneNumber());
        setEmail(editedUser.getEmail());
        setLocation(editedUser.getLocation());
        setCarOwner(editedUser.isCarOwner());
        setCarModel(editedUser.getCarModel());
        setCarPlate(editedUser.getCarPlate());
        setCarColor(editedUser.getCarColor());
    }

    public User(Parcel in) {
        String[] data = new String[13];
        in.readStringArray(data);

        name = data[0];
        profile = data[1];
        course = data[2];
        phoneNumber = data[3];
        email = data[4];
        location = data[5];
        carModel = data[6];
        carColor = data[7];
        carPlate = data[8];
        createdAt = data[9];
        profilePicUrl = data[10];
        faceId = data[11];
        idUfrj = data[12];

        int[] intData = new int[2];
        in.readIntArray(intData);
        carOwner = intData[0] == 1;
        dbId = intData[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{name,
                profile,
                course,
                phoneNumber,
                email,
                location,
                carModel,
                carColor,
                carPlate,
                createdAt,
                profilePicUrl,
                faceId,
                idUfrj});
        parcel.writeIntArray(new int[]{
                carOwner ? 1 : 0,
                dbId
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
