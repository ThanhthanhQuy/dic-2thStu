package sample;

public class Word {
    private String English;
    private String VietNam;
    public Word(String _English, String _VietNam) {
        English=_English;
        VietNam=_VietNam;
    }
    public String getEnglish() {
        return English;
    }

    public void setEnglish(String english) {
        English = english;
    }

    public String getVietNam() {
        return VietNam;
    }

    public void setVietNam(String vietNam) {
        VietNam = vietNam;
    }
}
