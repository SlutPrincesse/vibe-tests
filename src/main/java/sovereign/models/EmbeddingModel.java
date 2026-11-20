package sovereign.models;

public interface EmbeddingModel {
    float[] embed(String text) throws Exception;
}
