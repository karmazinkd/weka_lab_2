public class WordStats {

    public int upCount = 0;
    public int downCount = 0;
    public float upProbabilityRaw = 0.0f;
    public float downProbabilityRaw = 0.0f;
    public float upProbabilityNormalized = 0.0f;
    public float downProbabilityNormalized = 0.0f;


    public void addCount(boolean isPriceUp){
        if(isPriceUp)
            upCount++;
        else
            downCount++;

        //calculate raw probability:
        int sum = upCount+downCount;
        upProbabilityRaw = upCount / (float) sum;
        downProbabilityRaw = downCount / (float) sum;

        //normalize probability to avoid 0
        upProbabilityNormalized = (upCount * upProbabilityRaw + 0.5f)
                / (upCount + 1);
        downProbabilityNormalized = (downCount * downProbabilityRaw + 0.5f)
                / (downCount + 1);
    }

    @Override
    public String toString(){
        return "upCount: " + upCount + "; downCount: " + downCount +
                "\nupProbabilityRaw: " + upProbabilityRaw +
                "; downProbabilityRaw: " + downProbabilityRaw +
                "\nupProbabilityNormalized: " + upProbabilityNormalized +
                "; downProbabilityNormalized: " + downProbabilityNormalized;
    }
}
