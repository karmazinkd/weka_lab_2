import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String DATA_DELIMITER = ":::";
    private static final String PRICE_GOES_UP_INDICATOR = "up";

    public static void main(String[] args) throws Exception {

        //1. Parse data file into map of words with statistics
        HashMap<String, WordStats> mapOfWords =
                parseDataIntoMapOfWordsWithStats("gold_train.txt");

        //2. Print it (optionally)
        System.out.println("WORDS IN DICTIONARY (total " + mapOfWords.size()+"):");
        for(Map.Entry<String, WordStats> entry : mapOfWords.entrySet()){
            System.out.println("\n'" + entry.getKey() + "' - " + entry.getValue());
        }

        //3. Predict price movement by the given news headlight:
        String testHeadlightUp = "Next year we will hit new highs as gold demand rises";
        predictNaiveBayes(mapOfWords, testHeadlightUp);

        String testHeadlightDown = "Price falls as we sign a peace treaty with North " +
                "Korea - tweeted Donald Trump";
        predictNaiveBayes(mapOfWords, testHeadlightDown);
    }

    private static void predictNaiveBayes
            (HashMap<String, WordStats> mapOfWords, String testHeadlight) {

        float upProbability = 0.5f;
        float downProbability = 0.5f;

        String[] testWords = testHeadlight.split(" ");

        for(String testWord : testWords){

            //skip words that we don't know
            if(!mapOfWords.containsKey(testWord))
                continue;

            upProbability = upProbability * mapOfWords.get(testWord)
                    .upProbabilityNormalized;
            downProbability = downProbability * mapOfWords.get(testWord)
                    .downProbabilityNormalized;
        }

        System.out.println("\nPrediction for text: '" + testHeadlight +
                "'\nGOLD PRICE will go "+ (upProbability > downProbability ? "UP" : "DOWN"));
        System.out.println("up (" + upProbability + "); down (" + downProbability + ")");
    }

    private static HashMap<String, WordStats> parseDataIntoMapOfWordsWithStats
            (String dataFile) throws IOException {
        //Create a map of words with stats
        HashMap<String, WordStats> mapOfWords = new HashMap<>();
        //Parse data file and fill map with words
        File file = new File(dataFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String dataLine;
        String[] dataArray;
        String textLine;
        boolean isUpWord;
        String[] words;
        WordStats wordStats;
        while ((dataLine = br.readLine()) != null) {
            dataArray = dataLine.split(DATA_DELIMITER);
            textLine = dataArray[0];

            //remove punctuation from strings:
            textLine = textLine.replace(",", "")
                    .replace(".", "")
                    .replace(":", "")
                    .replace("-", "");

            isUpWord = dataArray[1].trim().equalsIgnoreCase(PRICE_GOES_UP_INDICATOR);
            //split text lines by each word:
            words = textLine.split(" ");

            for (String word : words) {
                if(word.isEmpty() || word.length() < 4 ) //remove empty and small words
                    continue;

                //re-calculate statistics for this word
                wordStats = mapOfWords.get(word);
                if (wordStats == null)
                    wordStats = new WordStats();
                wordStats.addCount(isUpWord);

                //add/update the word in our map
                mapOfWords.put(word, wordStats);
            }
        }
        return mapOfWords;
    }

}




/*
    //init our data sets
        DataSource trainData = new DataSource("gold_train.arff");
        Instances train = trainData.getDataSet();
        //set which attribute is outcome:
        train.setClassIndex(train.numAttributes() - 1);
        System.out.println("Train data set size: " + train.size());

        DataSource testData = new DataSource("gold_test.arff");
        Instances test = testData.getDataSet();
        test.setClassIndex(test.numAttributes() - 1);
        System.out.println("Train data set size: " + test.size());

        //build a model
//        Classifier cls = new J48();
//        cls.buildClassifier(train);
//        Correctly Classified Instances          19               86.3636 %
//        Incorrectly Classified Instances         3               13.6364 %

        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(train);

        //test it
        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(nb, test);

        System.out.println("Results summary:" + eval.toSummaryString(true));


  /*      DataSource source = new DataSource("iris.arff");
        Instances dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes()-1);
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(dataset);
        Evaluation eval = new Evaluation(dataset);
        eval.evaluateModel(nb, dataset);
        System.out.println(eval.toSummaryString());

        SMO svm = new SMO();
        svm.buildClassifier(dataset);
        Evaluation eval2 = new Evaluation(dataset);
        eval2.evaluateModel(svm, dataset);
        System.out.println(eval2.toSummaryString());

        LibSVM libsvm = new LibSVM();
        String[] options = new String[8];
        options[0] = "-S"; options[1] = "0";
        options[2] ="-K"; options[3] = "2";
        options[4] = "-G"; options[5] = "1.0";
        options[6] = "-C"; options[7] = "1.0";
        libsvm.setOptions(options);
        libsvm.buildClassifier(dataset);
        Evaluation eval3 = new Evaluation(dataset);
        eval3.evaluateModel(libsvm, dataset);
        System.out.println(eval3.toSummaryString());*/