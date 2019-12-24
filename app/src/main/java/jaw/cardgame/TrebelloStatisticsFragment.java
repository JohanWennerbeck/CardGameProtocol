package jaw.cardgame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jaw.cardgame.util.PlayerConverterJson;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrebelloStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrebelloStatisticsFragment extends Fragment {

    private Context mContext;
    private TextView name1, first1, second1, third1, highscore1, jumboscore1;
    private TextView name2, first2, second2, third2, highscore2, jumboscore2;
    private TextView name3, first3, second3, third3, highscore3, jumboscore3;
    Player player1;
    Player player2;
    Player player3;
    public static TrebelloStatisticsFragment newInstance() {
        TrebelloStatisticsFragment fragment = new TrebelloStatisticsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.trebello_statistics, container, false);
        initView(v, savedInstanceState);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView(View v, Bundle savedInstanceState) {
        player1 = new Player("Fredrik");
        player2 = new Player("Anders");
        player3 = new Player("Johan");
        player1.load(mContext, player1.getName());
        player2.load(mContext, player2.getName());
        player3.load(mContext, player3.getName());

        initTextViews(v);
        initStatistics();


    }

    void initTextViews(View v){
        name1 = v.findViewById(R.id.stat_name1);
        first1 = v.findViewById(R.id.stat_first1);
        second1 = v.findViewById(R.id.stat_second1) ;
        third1 = v.findViewById(R.id.stat_third1);
        highscore1 = v.findViewById(R.id.stat_highscore1);
        jumboscore1 = v.findViewById(R.id.stat_jumboscore1);

        name2 = v.findViewById(R.id.stat_name2);
        first2 = v.findViewById(R.id.stat_first2);
        second2 = v.findViewById(R.id.stat_second2) ;
        third2 = v.findViewById(R.id.stat_third2);
        highscore2 = v.findViewById(R.id.stat_highscore2);
        jumboscore2 = v.findViewById(R.id.stat_jumboscore2);

        name3 = v.findViewById(R.id.stat_name3);
        first3 = v.findViewById(R.id.stat_first3);
        second3 = v.findViewById(R.id.stat_second3) ;
        third3 = v.findViewById(R.id.stat_third3);
        highscore3 = v.findViewById(R.id.stat_highscore3);
        jumboscore3 = v.findViewById(R.id.stat_jumboscore3);
    }

    void initStatistics(){
        name1.setText(player1.getName());
        first1.setText("First placements: " + player1.getTrebelloFirst());
        second1.setText("Second placements: " + player1.getTrebelloSecond());
        third1.setText("Third placements: " + player1.getTrebelloThird());
        highscore1.setText("Highest score: " + player1.getTrebelloHighScore());
        jumboscore1.setText("Lowest score: " + player1.getTrebelloJumboScore());

        name2.setText(player2.getName());
        first2.setText("First placements: " + player2.getTrebelloFirst());
        second2.setText("Second placements: " + player2.getTrebelloSecond());
        third2.setText("Third placements: " + player2.getTrebelloThird());
        highscore2.setText("Highest score: " + player2.getTrebelloHighScore());
        jumboscore2.setText("Lowest score: " + player2.getTrebelloJumboScore());

        name3.setText(player3.getName());
        first3.setText("First placements: " + player3.getTrebelloFirst());
        second3.setText("Second placements: " + player3.getTrebelloSecond());
        third3.setText("Third placements: " + player3.getTrebelloThird());
        highscore3.setText("Highest score: " + player3.getTrebelloHighScore());
        jumboscore3.setText("Lowest score: " + player3.getTrebelloJumboScore());
    }
}
