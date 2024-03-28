import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Shape

object Button {

    @Composable
    fun CustomButton(text: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text)
        }
    }

    @Composable
    fun RoundedOutlinedTextField(
        modifier: Modifier = Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        shape: Shape = RoundedCornerShape(8.dp), // Default rounded corner shape
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = shape,
            modifier = modifier.padding(4.dp) // Add padding for the input field
        )
    }

}