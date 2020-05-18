package ro.pub.cs.systems.eim.practicaltest02;

public class SavedData {
    String eur;
    String usd;
    String time;

    public SavedData(String eur, String usd, String time) {
        this.eur = eur;
        this.usd = usd;
        this.time = time;
    }


    @Override
    public String toString() {
        return "SavedData{" +
                "eur='" + eur + '\'' +
                ", usd='" + usd + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
