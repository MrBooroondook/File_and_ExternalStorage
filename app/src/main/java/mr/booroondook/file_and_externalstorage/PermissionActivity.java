package mr.booroondook.file_and_externalstorage;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public abstract class PermissionActivity extends AppCompatActivity {
    abstract protected String[] getDesiredPermissions();  // Возвращает все нужные разрешения
    abstract protected void onPermissionDenied();
    abstract protected void onReady(Bundle state);

    private static final int REQUEST_PERMISSION = 1;
    private static final String STATE_IN_PERMISSION = "inPermission";
    private boolean isInPermission = false;
    private Bundle state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.state = savedInstanceState;

        if (state != null) {
            isInPermission = state.getBoolean(STATE_IN_PERMISSION, false);
        }

        if (hasAllPermissions(getDesiredPermissions())) {
            onReady(state);
        } else if (!isInPermission) {
            isInPermission = true;

            // ActivityCompat.requestPermissions - запрос разрешения у пользователя
            ActivityCompat.requestPermissions(this, netPermissions(getDesiredPermissions()),
                    REQUEST_PERMISSION);
        }
    }

    // Возвращает ответ пользователя о разрешениях
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        isInPermission = false;
        if (requestCode == REQUEST_PERMISSION) {
            if (hasAllPermissions(getDesiredPermissions())) {
                onReady(state);
            } else {
                onPermissionDenied();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState,
                                    @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(STATE_IN_PERMISSION, isInPermission);
    }

    private boolean hasAllPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    //WTF ????? - protected ?
    // Получено ли разрешение
    protected boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private String[] netPermissions(String[] wanted) {
        ArrayList<String> result = new ArrayList<>();
        for (String permission : wanted) {
            if (hasPermission(permission)) {
                result.add(permission);
            }
        }
        int size = result.size();
        return result.toArray(new String[size]);
    }
}
