package mr.booroondook.file_and_externalstorage;

import android.os.Bundle;
import android.widget.Toast;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends PermissionActivity {

    @Override
    protected String[] getDesiredPermissions() {
        return(new String[]{WRITE_EXTERNAL_STORAGE});
        // ***** WRITE_EXTERNAL_STORAGE *****
        // Начиная с уровня API19, это разрешение не требуется для чтения/записи файлов в каталогах
        // вашего приложения, возвращаемых Context.getExternalFilesDir(String)
        // и Context.getExternalCacheDir().
        // path: /sdcard/Android/data/<package>/files/<file_name>
        // file = new File(Objects.requireNonNull(getContext()).getExternalFilesDir(null), FILE_NAME);
    }

    //WTF Если в разрешении отказано
    @Override
    protected void onPermissionDenied() {
        Toast.makeText(this, R.string.msg_sorry, Toast.LENGTH_LONG).show();
        finish();
    }

    //WTF Если разрешение получено
    @Override
    protected void onReady(Bundle savedInstanceState) {
        setContentView(R.layout.main);
    }
}
