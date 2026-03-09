import java.util.Map;

public interface Profile {
    String GetName();
    String GetAddress();
    Map<Profile, String> getRelationships();
}