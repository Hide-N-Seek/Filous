package hide_n_seek.com.filous;

/**
 * Created by computerfan9000 on 2/28/2016.
 */
public class Users {
        String interest1;
        String interest2;
        String interest3;
        String latitude;
        String first;
        String last;
        String age_range;
        String email;
        String gender;

        public Users() {
        }

        public Users(String interest1, String interest2, String interest3, String latitude,
                           String first, String last, String age_range, String email, String gender) {
            this.interest1 = interest1;
            this.interest2 = interest2;
            this.interest3 = interest3;
            this.latitude = latitude;
            this.first = first;
            this.last = last;
            this.age_range = age_range;
            this.email = email;
            this.gender = gender;

        }

    public String getAge_range() {
        return age_range;
    }

    public String getFirst() {
        return first;
    }

    public String getEmail() {
        return email;
    }

    public String getInterest1() {
        return interest1;
    }

    public String getGender() {
        return gender;
    }

    public String getInterest2() {
        return interest2;
    }

    public String getLast() {
        return last;
    }

    public String getInterest3() {
        return interest3;
    }


    public String getLatitude() {
        return latitude;
    }
}
