package jaw.cardgame;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import jaw.cardgame.util.StorageUtil;
import jaw.cardgame.util.TribelloGameConverterJson;

class TribelloGameModel {

    private int playerOneScore, playerTwoScore, playerThreeScore;
    private int playerOneTotalScore, playerTwoTotalScore, playerThreeTotalScore;
    private ArrayList<Integer> playerOneScoreArray;
    private ArrayList<Integer> playerTwoScoreArray;
    private ArrayList<Integer> playerThreeScoreArray;
    private int round;
    private final boolean[][] checkedGameOptions = new boolean[3][4];

    private final Player playerOne;
    private final Player playerTwo;
    private final Player playerThree;

    private static final String STATE_ROUND = "round";
    private static final String STATE_PLAYER_ONE_SCORE = "playerOneScore";
    private static final String STATE_PLAYER_TWO_SCORE = "playerTwoScore";
    private static final String STATE_PLAYER_THREE_SCORE = "playerThreeScore";
    private static final String STATE_PLAYER_ONE_SCORE_ARRAY = "playerOneScoreArray";
    private static final String STATE_PLAYER_TWO_SCORE_ARRAY = "playerTwoScoreArray";
    private static final String STATE_PLAYER_THREE_SCORE_ARRAY = "playerThreeScoreArray";
    private static final String STATE_PLAYER_ONE_ROUND_SCORE = "playerOneRoundScore";
    private static final String STATE_PLAYER_TWO_ROUND_SCORE = "playerTwoRoundScore";
    private static final String STATE_PLAYER_THREE_ROUND_SCORE = "playerThreeRoundScore";
    private static final String STATE_GAME_OPTION_ONE = "gameOptionOne";
    private static final String STATE_GAME_OPTION_TWO = "gameOptionTwo";
    private static final String STATE_GAME_OPTION_THREE = "gameOptionThree";
    private static final String STATE_PLAYER_ONE_NAME = "playerOneName";
    private static final String STATE_PLAYER_TWO_NAME = "playerTwoName";
    private static final String STATE_PLAYER_THREE_NAME = "playerThreeName";

    TribelloGameModel(Bundle bundleArguments, Context context){
        playerOneScore = 0;
        playerTwoScore = 0;
        playerThreeScore = 0;
        playerOneTotalScore = 0;
        playerTwoTotalScore = 0;
        playerThreeTotalScore = 0;
        round = 1;
        if(bundleArguments.getStringArrayList("playerNames") != null){
            playerOne = new Player(Objects.requireNonNull(bundleArguments.getStringArrayList("playerNames")).get(0));
            playerOne.load(context, Objects.requireNonNull(bundleArguments.getStringArrayList("playerNames")).get(0) );
            playerTwo = new Player(Objects.requireNonNull(bundleArguments.getStringArrayList("playerNames")).get(1));
            playerTwo.load(context, Objects.requireNonNull(bundleArguments.getStringArrayList("playerNames")).get(1));
            playerThree = new Player(Objects.requireNonNull(bundleArguments.getStringArrayList("playerNames")).get(2));
            playerThree.load(context, Objects.requireNonNull(bundleArguments.getStringArrayList("playerNames")).get(2));
        } else {
            playerOne = new Player();
            playerTwo = new Player();
            playerThree = new Player();
        }
        playerOneScoreArray = new ArrayList<>(Collections.nCopies(0,12));
        playerTwoScoreArray = new ArrayList<>(Collections.nCopies(0,12));
        playerThreeScoreArray = new ArrayList<>(Collections.nCopies(0,12));
    }

    void setSavedInstanceState(Bundle savedInstanceState){
        round = savedInstanceState.getInt(STATE_ROUND);
        playerOneTotalScore = savedInstanceState.getInt(STATE_PLAYER_ONE_SCORE);
        playerTwoTotalScore = savedInstanceState.getInt(STATE_PLAYER_TWO_SCORE);
        playerThreeTotalScore = savedInstanceState.getInt(STATE_PLAYER_THREE_SCORE);
        playerOneScoreArray = savedInstanceState.getIntegerArrayList(STATE_PLAYER_ONE_SCORE_ARRAY);
        playerTwoScoreArray = savedInstanceState.getIntegerArrayList(STATE_PLAYER_TWO_SCORE_ARRAY);
        playerThreeScoreArray = savedInstanceState.getIntegerArrayList(STATE_PLAYER_THREE_SCORE_ARRAY);
        playerOneScore = savedInstanceState.getInt(STATE_PLAYER_ONE_ROUND_SCORE);
        playerTwoScore = savedInstanceState.getInt(STATE_PLAYER_TWO_ROUND_SCORE);
        playerThreeScore = savedInstanceState.getInt(STATE_PLAYER_THREE_ROUND_SCORE);
        checkedGameOptions[0] = savedInstanceState.getBooleanArray(STATE_GAME_OPTION_ONE);
        checkedGameOptions[1] = savedInstanceState.getBooleanArray(STATE_GAME_OPTION_TWO);
        checkedGameOptions[2] = savedInstanceState.getBooleanArray(STATE_GAME_OPTION_THREE);
        playerOne.setName(savedInstanceState.getString(STATE_PLAYER_ONE_NAME));
        playerTwo.setName(savedInstanceState.getString(STATE_PLAYER_TWO_NAME));
        playerThree.setName(savedInstanceState.getString(STATE_PLAYER_THREE_NAME));
    }

    Bundle onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(STATE_ROUND, round);
        savedInstanceState.putInt(STATE_PLAYER_ONE_SCORE, playerOneTotalScore);
        savedInstanceState.putInt(STATE_PLAYER_TWO_SCORE, playerTwoTotalScore);
        savedInstanceState.putInt(STATE_PLAYER_THREE_SCORE, playerThreeTotalScore);
        savedInstanceState.putIntegerArrayList(STATE_PLAYER_ONE_SCORE_ARRAY, playerOneScoreArray);
        savedInstanceState.putIntegerArrayList(STATE_PLAYER_TWO_SCORE_ARRAY, playerTwoScoreArray);
        savedInstanceState.putIntegerArrayList(STATE_PLAYER_THREE_SCORE_ARRAY, playerThreeScoreArray);
        savedInstanceState.putInt(STATE_PLAYER_ONE_ROUND_SCORE, playerOneScore);
        savedInstanceState.putInt(STATE_PLAYER_TWO_ROUND_SCORE, playerTwoScore);
        savedInstanceState.putInt(STATE_PLAYER_THREE_ROUND_SCORE, playerThreeScore);
        savedInstanceState.putBooleanArray(STATE_GAME_OPTION_ONE, checkedGameOptions[0]);
        savedInstanceState.putBooleanArray(STATE_GAME_OPTION_TWO, checkedGameOptions[1]);
        savedInstanceState.putBooleanArray(STATE_GAME_OPTION_THREE, checkedGameOptions[2]);
        savedInstanceState.putString(STATE_PLAYER_ONE_NAME, playerOne.getName());
        savedInstanceState.putString(STATE_PLAYER_TWO_NAME, playerTwo.getName());
        savedInstanceState.putString(STATE_PLAYER_THREE_NAME, playerThree.getName());

        return savedInstanceState;
    }

    void save(Context context){
        savePlayers(context);
        JsonArray array1 = TribelloGameConverterJson.getInstance().toJson(playerOneScoreArray, "Score");
        JsonArray array2 = TribelloGameConverterJson.getInstance().toJson(playerTwoScoreArray, "Score");
        JsonArray array3 = TribelloGameConverterJson.getInstance().toJson(playerThreeScoreArray, "Score");
        JsonArray array4 = TribelloGameConverterJson.getInstance().toJson(checkedGameOptions[0], "GameOption");
        JsonArray array5 = TribelloGameConverterJson.getInstance().toJson(checkedGameOptions[1], "GameOption");
        JsonArray array6 = TribelloGameConverterJson.getInstance().toJson(checkedGameOptions[2], "GameOption");
        JsonObject object1 = TribelloGameConverterJson.getInstance().toJson(round, "Round");
        JsonObject object2 = TribelloGameConverterJson.getInstance().toJson(playerOneTotalScore, "PlayerOneScore");
        JsonObject object3 = TribelloGameConverterJson.getInstance().toJson(playerTwoTotalScore, "PlayerTwoScore");
        JsonObject object4 = TribelloGameConverterJson.getInstance().toJson(playerThreeTotalScore, "PlayerThreeScore");
        JsonObject object5 = TribelloGameConverterJson.getInstance().toJson(playerOne.getName(), "PlayerOneName");
        JsonObject object6 = TribelloGameConverterJson.getInstance().toJson(playerTwo.getName(), "PlayerTwoName");
        JsonObject object7 = TribelloGameConverterJson.getInstance().toJson(playerThree.getName(), "PlayerThreeName");

        StorageUtil.save(context.getApplicationContext(), "PlayerOneScoreArray" ,array1);
        StorageUtil.save(context.getApplicationContext(), "PlayerTwoScoreArray" ,array2);
        StorageUtil.save(context.getApplicationContext(), "PlayerThreeScoreArray" ,array3);
        StorageUtil.save(context.getApplicationContext(), "GameOption0" ,array4);
        StorageUtil.save(context.getApplicationContext(), "GameOption1" ,array5);
        StorageUtil.save(context.getApplicationContext(), "GameOption2" ,array6);
        StorageUtil.save(context.getApplicationContext(), "Round", object1);
        StorageUtil.save(context.getApplicationContext(), "PlayerOneScore", object2);
        StorageUtil.save(context.getApplicationContext(), "PlayerTwoScore", object3);
        StorageUtil.save(context.getApplicationContext(), "PlayerThreeScore", object4);
        StorageUtil.save(context.getApplicationContext(), "PlayerOneName", object5);
        StorageUtil.save(context.getApplicationContext(), "PlayerTwoName", object6);
        StorageUtil.save(context.getApplicationContext(), "PlayerThreeName", object7);

    }

    private void savePlayers(Context context){
        playerOne.save(context, playerOne.getName());
        playerTwo.save(context, playerTwo.getName());
        playerThree.save(context, playerThree.getName());
    }

    void load(Context context){
        JsonElement element1 = null;
        JsonElement element2 = null;
        JsonElement element3 = null;
        JsonElement element4 = null;
        JsonElement element5 = null;
        JsonElement element6 = null;
        JsonElement element7 = null;
        JsonElement element8 = null;
        JsonElement element9 = null;
        JsonElement element10 = null;
        JsonElement element11 = null;
        JsonElement element12 = null;
        JsonElement element13 = null;
        try {
            element1 = StorageUtil.load(context.getApplicationContext(), "PlayerOneScoreArray");
            element2 = StorageUtil.load(context.getApplicationContext(), "PlayerTwoScoreArray");
            element3 = StorageUtil.load(context.getApplicationContext(), "PlayerThreeScoreArray");
            element4 = StorageUtil.load(context.getApplicationContext(), "Round");
            element5 = StorageUtil.load(context.getApplicationContext(), "PlayerOneScore");
            element6 = StorageUtil.load(context.getApplicationContext(), "PlayerTwoScore");
            element7 = StorageUtil.load(context.getApplicationContext(), "PlayerThreeScore");
            element8 = StorageUtil.load(context.getApplicationContext(), "GameOption0");
            element9 = StorageUtil.load(context.getApplicationContext(), "GameOption1");
            element10 = StorageUtil.load(context.getApplicationContext(), "GameOption2");
            element11 = StorageUtil.load(context.getApplicationContext(), "PlayerOneName");
            element12 = StorageUtil.load(context.getApplicationContext(), "PlayerTwoName");
            element13 = StorageUtil.load(context.getApplicationContext(), "PlayerThreeName");
        } catch (IllegalStateException e) {
            StorageUtil.resetData(context.getApplicationContext(), "PlayerOneArray");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerTwoArray");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerThreeArray");
            StorageUtil.resetData(context.getApplicationContext(), "GameOption0");
            StorageUtil.resetData(context.getApplicationContext(), "GameOption1");
            StorageUtil.resetData(context.getApplicationContext(), "GameOption2");
            StorageUtil.resetData(context.getApplicationContext(), "Round");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerOneScore");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerTwoScore");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerThreeScore");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerOneName");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerTwoName");
            StorageUtil.resetData(context.getApplicationContext(), "PlayerThreeName");
        } catch (FileNotFoundException ignored) {
        }

        if ((element1 == null) || !element1.isJsonArray()
                || (element2 == null) || !element2.isJsonArray()
                || (element3 == null) || !element3.isJsonArray()
                || (element4 == null) || !element4.isJsonObject()
                || (element5 == null) || !element5.isJsonObject()
                || (element6 == null) || !element6.isJsonObject()
                || (element7 == null) || !element7.isJsonObject()
                || (element8 == null) || !element8.isJsonArray()
                || (element9 == null) || !element9.isJsonArray()
                || (element10 == null) || !element10.isJsonArray()
                || (element11 == null) || !element11.isJsonObject()
                || (element12 == null) || !element12.isJsonObject()
                || (element13 == null) || !element13.isJsonObject()) {
            return;
        }
        JsonArray array1 = element1.getAsJsonArray();
        JsonArray array2 = element2.getAsJsonArray();
        JsonArray array3 = element3.getAsJsonArray();
        JsonObject object1 = element4.getAsJsonObject();
        JsonObject object2 = element5.getAsJsonObject();
        JsonObject object3 = element6.getAsJsonObject();
        JsonObject object4 = element7.getAsJsonObject();
        JsonArray array4 = element8.getAsJsonArray();
        JsonArray array5 = element9.getAsJsonArray();
        JsonArray array6 = element10.getAsJsonArray();
        JsonObject object5 = element11.getAsJsonObject();
        JsonObject object6 = element12.getAsJsonObject();
        JsonObject object7 = element13.getAsJsonObject();

        playerOneScoreArray = TribelloGameConverterJson.getInstance().toObject(array1, "Score");
        playerTwoScoreArray = TribelloGameConverterJson.getInstance().toObject(array2, "Score");
        playerThreeScoreArray = TribelloGameConverterJson.getInstance().toObject(array3, "Score");
        round = TribelloGameConverterJson.getInstance().toObjectInt(object1, "Round");
        playerOneTotalScore = TribelloGameConverterJson.getInstance().toObjectInt(object2, "PlayerOneScore");
        playerTwoTotalScore = TribelloGameConverterJson.getInstance().toObjectInt(object3, "PlayerTwoScore");
        playerThreeTotalScore = TribelloGameConverterJson.getInstance().toObjectInt(object4, "PlayerThreeScore");
        checkedGameOptions[0] = TribelloGameConverterJson.getInstance().toObjectBool(array4);
        checkedGameOptions[1] = TribelloGameConverterJson.getInstance().toObjectBool(array5);
        checkedGameOptions[2] = TribelloGameConverterJson.getInstance().toObjectBool(array6);
        playerOne.setName(TribelloGameConverterJson.getInstance().toObjectString(object5, "PlayerOneName"));
        playerTwo.setName(TribelloGameConverterJson.getInstance().toObjectString(object6, "PlayerTwoName"));
        playerThree.setName(TribelloGameConverterJson.getInstance().toObjectString(object7, "PlayerThreeName"));

        loadPlayers(context);
    }

    private void loadPlayers(Context context){
        playerOne.load(context, playerOne.getName());
        playerTwo.load(context, playerTwo.getName());
        playerThree.load(context, playerThree.getName());
    }

    void updateScore(){
        playerOneTotalScore += playerOneScore;
        playerTwoTotalScore += playerTwoScore;
        playerThreeTotalScore += playerThreeScore;
        playerOneScoreArray.add(playerOneTotalScore);
        playerTwoScoreArray.add(playerTwoTotalScore);
        playerThreeScoreArray.add(playerThreeTotalScore);
    }

    void nextRound(){
        round++;
        playerOneScore = 0;
        playerTwoScore = 0;
        playerThreeScore = 0;
    }

    void newGame(Context context){
        StorageUtil.resetData(context.getApplicationContext(), "PlayerOneArray");
        StorageUtil.resetData(context.getApplicationContext(), "PlayerTwoArray");
        StorageUtil.resetData(context.getApplicationContext(), "PlayerThreeArray");
        StorageUtil.resetData(context.getApplicationContext(), "GameOption0");
        StorageUtil.resetData(context.getApplicationContext(), "GameOption1");
        StorageUtil.resetData(context.getApplicationContext(), "GameOption2");
        StorageUtil.resetData(context.getApplicationContext(), "Round");
        StorageUtil.resetData(context.getApplicationContext(), "PlayerOneScore");
        StorageUtil.resetData(context.getApplicationContext(), "PlayerTwoScore");
        StorageUtil.resetData(context.getApplicationContext(), "PlayerThreeScore");
        playerOneScore = 0;
        playerTwoScore = 0;
        playerThreeScore = 0;
        playerOneTotalScore = 0;
        playerTwoTotalScore = 0;
        playerThreeTotalScore = 0;
        round = 1;
        playerOneScoreArray = new ArrayList<>(Collections.nCopies(0,12));
        playerTwoScoreArray = new ArrayList<>(Collections.nCopies(0,12));
        playerThreeScoreArray = new ArrayList<>(Collections.nCopies(0,12));
    }

    void addStatistics(Context context) {
        if(playerOneTotalScore > playerTwoTotalScore ){
            if (playerTwoTotalScore > playerThreeTotalScore) {
                playerOne.setTribelloFirst(playerOne.getTribelloFirst()+1);
                playerTwo.setTribelloSecond(playerTwo.getTribelloSecond()+1);
                playerThree.setTribelloThird(playerThree.getTribelloThird()+1);
            } else if (playerTwoTotalScore < playerThreeTotalScore) {
                if (playerOneTotalScore > playerThreeTotalScore) {
                    playerOne.setTribelloFirst(playerOne.getTribelloFirst()+1);
                    playerTwo.setTribelloThird(playerTwo.getTribelloThird()+1);
                    playerThree.setTribelloSecond(playerThree.getTribelloSecond()+1);
                } else if ( playerOneTotalScore < playerThreeTotalScore){
                    playerOne.setTribelloSecond(playerOne.getTribelloSecond()+1);
                    playerTwo.setTribelloThird(playerTwo.getTribelloThird()+1);
                    playerThree.setTribelloFirst(playerThree.getTribelloFirst()+1);
                } else {
                    playerOne.setTribelloFirst(playerOne.getTribelloFirst()+1);
                    playerTwo.setTribelloThird(playerTwo.getTribelloThird()+1);
                    playerThree.setTribelloFirst(playerThree.getTribelloFirst()+1);
                }
            } else {
                playerOne.setTribelloFirst(playerOne.getTribelloFirst()+1);
                playerTwo.setTribelloSecond(playerTwo.getTribelloSecond()+1);
                playerThree.setTribelloSecond(playerThree.getTribelloSecond()+1);
            }
        } else if (playerOneTotalScore < playerTwoTotalScore) {
            if (playerOneTotalScore > playerThreeTotalScore) {
                playerOne.setTribelloSecond(playerOne.getTribelloSecond()+1);
                playerTwo.setTribelloFirst(playerTwo.getTribelloFirst()+1);
                playerThree.setTribelloThird(playerThree.getTribelloThird()+1);
            } else if (playerOneTotalScore < playerThreeTotalScore) {
                if (playerTwoTotalScore > playerThreeTotalScore) {
                    playerOne.setTribelloThird(playerOne.getTribelloThird()+1);
                    playerTwo.setTribelloFirst(playerTwo.getTribelloFirst()+1);
                    playerThree.setTribelloSecond(playerThree.getTribelloSecond()+1);
                } else if (playerTwoTotalScore < playerThreeTotalScore) {
                    playerOne.setTribelloThird(playerOne.getTribelloThird()+1);
                    playerTwo.setTribelloSecond(playerTwo.getTribelloSecond()+1);
                    playerThree.setTribelloFirst(playerThree.getTribelloFirst()+1);
                } else {
                    playerOne.setTribelloThird(playerOne.getTribelloThird()+1);
                    playerTwo.setTribelloFirst(playerTwo.getTribelloFirst()+1);
                    playerThree.setTribelloFirst(playerThree.getTribelloFirst()+1);
                }
            } else {
                playerOne.setTribelloSecond(playerOne.getTribelloSecond()+1);
                playerTwo.setTribelloFirst(playerTwo.getTribelloFirst()+1);
                playerThree.setTribelloSecond(playerThree.getTribelloSecond()+1);
            }
        } else {
            if (playerOneTotalScore > playerThreeTotalScore) {
                playerOne.setTribelloFirst(playerOne.getTribelloFirst()+1);
                playerTwo.setTribelloFirst(playerTwo.getTribelloFirst()+1);
                playerThree.setTribelloThird(playerThree.getTribelloThird()+1);
            } else if (playerOneTotalScore < playerThreeTotalScore) {
                playerOne.setTribelloSecond(playerOne.getTribelloSecond()+1);
                playerTwo.setTribelloSecond(playerTwo.getTribelloSecond()+1);
                playerThree.setTribelloFirst(playerThree.getTribelloFirst()+1);
            } else {
                playerOne.setTribelloFirst(playerOne.getTribelloFirst()+1);
                playerTwo.setTribelloFirst(playerTwo.getTribelloFirst()+1);
                playerThree.setTribelloFirst(playerThree.getTribelloFirst()+1);
            }
        }

        if (playerOneTotalScore > playerOne.getTribelloHighScore()){
            playerOne.setTribelloHighScore(playerOneTotalScore);
        }
        if (playerOneTotalScore < playerOne.getTribelloJumboScore()){
            playerOne.setTribelloJumboScore(playerOneTotalScore);
        }
        if (playerTwoTotalScore > playerTwo.getTribelloHighScore()){
            playerTwo.setTribelloHighScore(playerTwoTotalScore);
        }
        if (playerTwoTotalScore < playerTwo.getTribelloJumboScore()){
            playerTwo.setTribelloJumboScore(playerTwoTotalScore);
        }
        if (playerThreeTotalScore > playerThree.getTribelloHighScore()){
            playerThree.setTribelloHighScore(playerThreeTotalScore);
        }
        if (playerThreeTotalScore < playerThree.getTribelloJumboScore()){
            playerThree.setTribelloJumboScore(playerThreeTotalScore);
        }
        savePlayers(context);
    }

    int getPlayerOneScore() {
        return playerOneScore;
    }

    void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    int getPlayerTwoScore() {
        return playerTwoScore;
    }

    void setPlayerTwoScore(int playerTwoScore) {
        this.playerTwoScore = playerTwoScore;
    }

    int getPlayerThreeScore() {
        return playerThreeScore;
    }

    void setPlayerThreeScore(int playerThreeScore) {
        this.playerThreeScore = playerThreeScore;
    }

    int getPlayerOneTotalScore() {
        return playerOneTotalScore;
    }

    int getPlayerTwoTotalScore() {
        return playerTwoTotalScore;
    }

    int getPlayerThreeTotalScore() {
        return playerThreeTotalScore;
    }

    ArrayList<Integer> getPlayerOneScoreArray() {
        return playerOneScoreArray;
    }

    ArrayList<Integer> getPlayerTwoScoreArray() {
        return playerTwoScoreArray;
    }

    ArrayList<Integer> getPlayerThreeScoreArray() {
        return playerThreeScoreArray;
    }

    int getRound() {
        return round;
    }

    boolean[][] getCheckedGameOptions() {
        return checkedGameOptions;
    }

    void setCheckedGameOption(int i, int j, boolean bool){
        this.checkedGameOptions[i][j] = bool;
    }

    Player getPlayerOne() {
        return playerOne;
    }

    Player getPlayerTwo() {
        return playerTwo;
    }

    Player getPlayerThree() {
        return playerThree;
    }
}
