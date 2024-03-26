import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

object Button {

    @Composable
    fun CustomButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Custom Button")
        }
    }

}