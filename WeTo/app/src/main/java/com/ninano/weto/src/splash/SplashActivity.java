package com.ninano.weto.src.splash;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ninano.weto.R;
import com.ninano.weto.db.AppDatabase;
import com.ninano.weto.db.ToDoDao;
import com.ninano.weto.db.ToDoWithData;
import com.ninano.weto.src.BaseActivity;
import com.ninano.weto.src.main.MainActivity;

import java.util.List;

import static com.ninano.weto.src.ApplicationClass.getApplicationClassContext;
import static com.ninano.weto.src.common.Geofence.GeofenceMaker.getGeofenceMaker;

public class SplashActivity extends BaseActivity {

    private ToDoDao mTodoDao;
    private AppDatabase mDatabase = AppDatabase.getAppDatabase(getApplicationClassContext());
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        getGeofenceMaker().removeAllGeofence();

        SplashAsyncTask splashAsyncTask = new SplashAsyncTask(mDatabase.todoDao());
        splashAsyncTask.execute();
    }

    //비동기처리                                   //넘겨줄객체, 중간에 처리할 데이터, 결과물(return)
    private class SplashAsyncTask extends AsyncTask<Void, Void, List<ToDoWithData>> {
        private ToDoDao mTodoDao;

        SplashAsyncTask(ToDoDao mTodoDao) {
            this.mTodoDao = mTodoDao;
        }

        @Override
        protected List<ToDoWithData> doInBackground(Void... voids) {
            return mTodoDao.getActivatedTodoListNoLive();
        }

        @Override
        protected void onPostExecute(List<ToDoWithData> toDoWithDataList) {
            super.onPostExecute(toDoWithDataList);
            if(toDoWithDataList.size()==0){
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
            getGeofenceMaker().addGeoFenceList(toDoWithDataList, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showCustomToast("지오펜스 재등록에 실패");
                }
            });
        }


    }
}