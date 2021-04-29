package pitch.tools;

public class FileSuffix {
        public String getFileCorrespondence(int index) throws Exception {
        String value = "";

        switch (index) {
            case 0:
                value = "ln";
                break;
            case 1:
                value = "rn";
                break;
            case 2:
                value = "ls";
                break;
            case 3:
                value = "rs";
                break;
            case 4:
                value = "lw";
                break;
            case 5:
                value = "rw";
                break;
            case 6:
                value = "lm";
                break;
            case 7:
                value = "rm";
                break;
            default:
                throw new Exception("Unknown index for pitch correpondance: " + index + "!");
        }

        return "Pitch_" + value;
    }
}
