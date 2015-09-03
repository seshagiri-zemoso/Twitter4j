import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import java.io.IOException;



class TweetsGeo {


   public static void main(String[] args)throws IOException{
   
   
       ConfigurationBuilder cb = new ConfigurationBuilder();
       cb.setDebugEnabled(true);
       cb.setOAuthConsumerKey("GyU44mOHtFBkMF3NUHYAS8aSE");
       cb.setOAuthConsumerSecret("mpClguqq6bFYdOG07LInGBzb079ZV2uwk0PGLzhRt7UjHyncPm");
       cb.setOAuthAccessToken("3242300713-XlO68b7fWAezI2NOBG1ZcitJojI9JSAWcoCJWOk");
       cb.setOAuthAccessTokenSecret("LXfDDHDZLi2wbYhzp9gbrPTP5mqoRu48MnaKl0vRqDLG6");

       TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

       StatusListener listener = new StatusListener() {

           public void onException(Exception arg0) {
               // TODO Auto-generated method stub

           }

           public void onDeletionNotice(StatusDeletionNotice arg0) {
               // TODO Auto-generated method stub

           }

           
           public void onScrubGeo(long arg0, long arg1) {
               // TODO Auto-generated method stub

           }

           
           public void onStatus(Status status){
   
            User user = status.getUser();
               
               // gets Username
               String username = status.getUser().getScreenName();
               System.out.println(username);
               System.out.println(status.getCreatedAt());
               String profileLocation = user.getLocation();
               System.out.println(profileLocation);
               long tweetId = status.getId();
               System.out.println(tweetId);
               String content = status.getText();
               System.out.println(content +"\n");
               

           }
     
           
           public void onTrackLimitationNotice(int arg0) {
               // TODO Auto-generated method stub

           }

public void onStallWarning(StallWarning arg0) {
// TODO Auto-generated method stub

}

       };
       
       FilterQuery fq = new FilterQuery();        
       fq.locations(new double[][]{new double[]{65.0000,6.0000},
               new double[]{97.3500,35.95}});        
       fq.language(new String[]{"en"});
       twitterStream.addListener(listener);
       twitterStream.filter(fq);
       

   }
}