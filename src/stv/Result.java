package stv;

public class Result {
    private Option mOption;
    private int mVotes;

    public Result(Option option, int initVotes) {
        mOption = option;
        mVotes = initVotes;
    }

    public Result(Option option) {
        this(option,0);
    }

    public void addVotes(int votes){
        if(votes > 0)
            mVotes += votes;
    }

    public void addVote() {
        addVotes(1);
    }

    public void removeVotes(int votes) {
        if(mVotes-votes >= 0)
            mVotes -= votes;
        else
            mVotes = 0;
    }

    public void removeVote() {
        removeVotes(1);
    }

    public Option getOption() {
        return mOption;
    }

    public int getVotes() {
        return mVotes;
    }
}
