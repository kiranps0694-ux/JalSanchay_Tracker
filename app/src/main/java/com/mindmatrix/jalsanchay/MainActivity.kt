package com.mindmatrix.jalsanchay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindmatrix.jalsanchay.data.local.entity.RainfallLogEntity
import com.mindmatrix.jalsanchay.data.local.entity.RoofMaterial
import com.mindmatrix.jalsanchay.ui.theme.Indigo
import com.mindmatrix.jalsanchay.ui.theme.JalSanchayTheme
import com.mindmatrix.jalsanchay.ui.theme.Pink
import com.mindmatrix.jalsanchay.ui.theme.Purple
import com.mindmatrix.jalsanchay.ui.theme.SoftLight
import com.mindmatrix.jalsanchay.ui.theme.SoftSlate
import com.mindmatrix.jalsanchay.ui.viewmodel.DashboardUiState
import com.mindmatrix.jalsanchay.ui.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkMode by rememberSaveable { mutableStateOf(false) }
            JalSanchayTheme(darkTheme = darkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GuidedJalSanchayApp(
                        darkMode = darkMode,
                        onDarkModeChange = { darkMode = it }
                    )
                }
            }
        }
    }
}

private enum class FlowScreen {
    WELCOME,
    SETUP,
    DATA,
    DASHBOARD,
    REPORT,
    IMPACT,
    TIPS
}

@Composable
private fun GuidedJalSanchayApp(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var screen by rememberSaveable { mutableStateOf(FlowScreen.WELCOME) }

    when (screen) {
        FlowScreen.WELCOME -> WelcomeScreen(darkMode, onDarkModeChange) { screen = FlowScreen.SETUP }
        FlowScreen.SETUP -> SetupScreen(
            darkMode = darkMode,
            onDarkModeChange = onDarkModeChange,
            uiState = uiState,
            onBack = { screen = FlowScreen.WELCOME }
        ) { name, roofArea, tankCapacity, material ->
            if (viewModel.saveSetup(name, roofArea, tankCapacity, material, "")) screen = FlowScreen.DATA
        }
        FlowScreen.DATA -> DataEntryScreen(
            darkMode = darkMode,
            onDarkModeChange = onDarkModeChange,
            uiState = uiState,
            onBack = { screen = FlowScreen.SETUP }
        ) { rainfall, notes ->
            if (viewModel.addRainfall(rainfall, notes)) screen = FlowScreen.DASHBOARD
        }
        FlowScreen.DASHBOARD -> DashboardScreen(
            darkMode = darkMode,
            onDarkModeChange = onDarkModeChange,
            uiState = uiState,
            onBack = { screen = FlowScreen.DATA },
            onNext = { screen = FlowScreen.REPORT }
        )
        FlowScreen.REPORT -> ReportScreen(
            darkMode = darkMode,
            onDarkModeChange = onDarkModeChange,
            uiState = uiState,
            onBack = { screen = FlowScreen.DASHBOARD },
            onNext = { screen = FlowScreen.IMPACT }
        )
        FlowScreen.IMPACT -> ImpactScreen(
            darkMode = darkMode,
            onDarkModeChange = onDarkModeChange,
            uiState = uiState,
            onBack = { screen = FlowScreen.REPORT },
            onNext = { screen = FlowScreen.TIPS }
        )
        FlowScreen.TIPS -> TipsScreen(
            darkMode = darkMode,
            onDarkModeChange = onDarkModeChange,
            onBack = { screen = FlowScreen.IMPACT },
            onRestart = { screen = FlowScreen.SETUP }
        )
    }
}

@Composable
private fun WelcomeScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onStart: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "welcome")
    val pulse by transition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "logoPulse"
    )
    val float by transition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2200), RepeatMode.Reverse),
        label = "float"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush())
            .padding(22.dp),
        contentAlignment = Alignment.Center
    ) {
        ParticleField()
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { it / 5 }
        ) {
            AppCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jal_sanchay_logo),
                        contentDescription = "Jal-Sanchay Tracker logo",
                        modifier = Modifier
                            .size(184.dp)
                            .graphicsLayer(
                                scaleX = pulse,
                                scaleY = pulse,
                                translationY = float
                            )
                    )
                    SustainabilityBadgeCompact()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Welcome to",
                            style = MaterialTheme.typography.displayLarge,
                            color = Indigo,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Jal-Sanchay",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color(0xFF00696E),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = "Measure rainfall, calculate water wealth, and help every household track conservation with confidence.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.74f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    DarkModeRow(darkMode, onDarkModeChange)
                    GradientButton("Start Tracking", onStart)
                    StitchSecondaryButton("Learn More") { }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetupScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    uiState: DashboardUiState,
    onBack: () -> Unit,
    onSave: (String, String, String, RoofMaterial) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var roofArea by rememberSaveable { mutableStateOf(uiState.roofArea.takeIf { it > 0 }?.toString() ?: "") }
    var tankCapacity by rememberSaveable { mutableStateOf(uiState.tankCapacity.takeIf { it > 0 }?.toString() ?: "") }
    var material by rememberSaveable { mutableStateOf(uiState.roofMaterial) }

    StitchFormShell(
        title = "Setup",
        subtitle = "Enter household details before recording rainfall.",
        darkMode = darkMode,
        onDarkModeChange = onDarkModeChange,
        onBack = onBack
    ) {
        AppCard {
            SectionBadge("HOUSEHOLD SETUP")
            PrettyTextField(name, { name = it }, "Name")
            PrettyTextField(
                value = roofArea,
                onValueChange = { roofArea = it },
                label = "Roof area",
                suffix = "sq ft",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            PrettyTextField(
                value = tankCapacity,
                onValueChange = { tankCapacity = it },
                label = "Tank capacity",
                suffix = "L",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Text("Roof material", fontWeight = FontWeight.SemiBold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RoofMaterial.values().take(2).forEach {
                        FilterChip(
                            selected = material == it,
                            onClick = { material = it },
                            label = { Text(it.displayName) }
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RoofMaterial.values().drop(2).forEach {
                        FilterChip(
                            selected = material == it,
                            onClick = { material = it },
                            label = { Text(it.displayName) }
                        )
                    }
                }
            }
            StitchPrimaryButton("Save and Continue  ->") { onSave(name, roofArea, tankCapacity, material) }
            MessageText(uiState.message)
        }
    }
}

@Composable
private fun DataEntryScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    uiState: DashboardUiState,
    onBack: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var rainfallInput by rememberSaveable { mutableStateOf("") }
    StitchFormShell(
        title = "Data Entry",
        subtitle = "Enter rainfall for the saved setup.",
        darkMode = darkMode,
        onDarkModeChange = onDarkModeChange,
        onBack = onBack
    ) {
        AppCard {
            SectionBadge("Rainfall")
            PrettyTextField(
                value = rainfallInput,
                onValueChange = { rainfallInput = it },
                label = "Rainfall",
                suffix = "mm",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            GradientButton("Calculate and Save") {
                onAdd(rainfallInput, "")
            }
            MessageText(uiState.message)
        }
    }
}

@Composable
private fun DashboardScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    uiState: DashboardUiState,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    StitchFormShell(
        title = "Dashboard",
        subtitle = "Live savings, tank progress, and rainfall history.",
        darkMode = darkMode,
        onDarkModeChange = onDarkModeChange,
        onBack = onBack
    ) {
        HeroSummary(uiState)
        WaterTankCard(uiState)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard("Today", "${uiState.todayLitres.roundToInt()} L", "drop", Modifier.weight(1f))
            MetricCard("Total", "${uiState.totalLitres.roundToInt()} L", "save", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(2.dp))
        GradientButton("View Report", onNext)
        Text("Recent Rainfall", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (uiState.recentEntries.isEmpty()) {
            EmptyState("No rainfall entries yet.")
        } else {
            uiState.recentEntries.forEach { RainfallRow(it) }
        }
    }
}

@Composable
private fun ReportScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    uiState: DashboardUiState,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val tankPercent = if (uiState.tankCapacity <= 0) 0 else ((uiState.monthlyLitres / uiState.tankCapacity) * 100).roundToInt()
    StitchFormShell(
        title = "Report",
        subtitle = "Current user monthly water summary.",
        darkMode = darkMode,
        onDarkModeChange = onDarkModeChange,
        onBack = onBack
    ) {
        AppCard {
            SectionBadge("Current User")
            ReportLine("Monthly saved", "${uiState.monthlyLitres.roundToInt()} L")
            ProgressBar((tankPercent / 100f).coerceIn(0f, 1f))
            ReportLine("Tank filled", "${tankPercent.coerceIn(0, 100)}%")
            ReportLine("Household water days", "%.1f".format(uiState.householdDays))
        }
        GradientButton("View Impact", onNext)
    }
}

@Composable
private fun ImpactScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    uiState: DashboardUiState,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    StitchFormShell(
        title = "Impact Score",
        subtitle = "Litres converted into household meaning.",
        darkMode = darkMode,
        onDarkModeChange = onDarkModeChange,
        onBack = onBack
    ) {
        MetricCard(
            label = "Household water days",
            value = "%.1f days".format(uiState.householdDays),
            icon = "home",
            modifier = Modifier.fillMaxWidth()
        )
        AppCard {
            SectionBadge("Water Security")
            ReportLine("Total water saved", "${uiState.totalLitres.roundToInt()} L")
            ReportLine("Daily use assumption", "135 L per person")
            Text(
                text = "Every entry makes conservation measurable and easier to explain in your internship demo.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        GradientButton("Read Tips", onNext)
    }
}

@Composable
private fun TipsScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    onRestart: () -> Unit
) {
    val tips = listOf(
        "Clean roof gutters before monsoon.",
        "Use a first-flush diverter for cleaner storage.",
        "Cover tanks tightly to reduce evaporation.",
        "Use stored rainwater first for gardening.",
        "Record rainfall monthly to plan water security."
    )
    StitchFormShell(
        title = "Tips",
        subtitle = "Better rainwater harvesting habits.",
        darkMode = darkMode,
        onDarkModeChange = onDarkModeChange,
        onBack = onBack
    ) {
        tips.forEachIndexed { index, tip ->
            AppCard {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconBubble("${index + 1}")
                    Text(tip, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        GradientButton("Add Another User", onRestart)
    }
}

@Composable
private fun StitchFormShell(
    title: String,
    subtitle: String,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBack: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundBrush())
            .padding(horizontal = 22.dp, vertical = 28.dp)
    ) {
        StitchHeaderBlobs()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 52.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackButton(onBack)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(if (darkMode) "Dark" else "Light", style = MaterialTheme.typography.labelSmall)
                        Switch(checked = darkMode, onCheckedChange = onDarkModeChange)
                    }
                }
            }
            item { SustainabilityBadge() }
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        color = Indigo,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = subtitle,
                        color = Color(0xFF424654),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp), content = content)
            }
        }
    }
}

@Composable
private fun BackButton(onBack: (() -> Unit)?) {
    if (onBack == null) {
        Spacer(modifier = Modifier.size(44.dp))
        return
    }
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.8f))
            .border(1.dp, Color(0xFFC3C6D6).copy(alpha = 0.7f), CircleShape)
            .clickable(onClick = onBack),
        contentAlignment = Alignment.Center
    ) {
        Text("<", color = Indigo, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun ScreenShell(
    title: String,
    subtitle: String,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(containerColor = Color.Transparent) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppBackgroundBrush()),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { HeaderCard(title, subtitle, darkMode, onDarkModeChange) }
            item { Column(verticalArrangement = Arrangement.spacedBy(14.dp), content = content) }
        }
    }
}

@Composable
private fun SustainabilityBadge() {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF00F4FE).copy(alpha = 0.18f))
            .border(1.dp, Color(0xFF00DCE5).copy(alpha = 0.55f), CircleShape)
            .padding(horizontal = 26.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("~~~", color = Color(0xFF00696E), fontWeight = FontWeight.Bold)
        Text(
            "SUSTAINABILITY FIRST",
            color = Color(0xFF00696E),
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
private fun SustainabilityBadgeCompact() {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF00F4FE).copy(alpha = 0.14f))
            .border(1.dp, Color(0xFF00DCE5).copy(alpha = 0.45f), CircleShape)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("~~~", color = Color(0xFF00696E), fontWeight = FontWeight.Bold)
        Text(
            "SUSTAINABILITY FIRST",
            color = Color(0xFF00696E),
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
private fun StitchPrimaryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(20.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF00F4FE), Color(0xFF0040A1))))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun StitchSecondaryButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(10.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.78f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Indigo, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun StitchBackgroundBlobs() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color(0xFF00F4FE).copy(alpha = 0.40f),
            radius = size.width * 0.44f,
            center = Offset(size.width * 0.20f, size.height * 0.12f)
        )
        drawCircle(
            color = Color(0xFFB2C5FF).copy(alpha = 0.42f),
            radius = size.width * 0.56f,
            center = Offset(size.width * 0.86f, size.height * 0.88f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.58f),
            radius = size.width * 0.48f,
            center = Offset(size.width * 0.58f, size.height * 0.40f)
        )
    }
}

@Composable
private fun StitchHeaderBlobs() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color(0xFF00F4FE).copy(alpha = 0.28f),
            radius = size.width * 0.26f,
            center = Offset(size.width * 0.16f, size.height * 0.10f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.34f),
            radius = size.width * 0.24f,
            center = Offset(size.width * 0.78f, size.height * 0.18f)
        )
    }
}

@Composable
private fun HeaderCard(
    title: String,
    subtitle: String,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.jal_sanchay_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
                Column {
                    GradientTitle(title)
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (darkMode) "Dark" else "Light", style = MaterialTheme.typography.labelSmall)
                Switch(checked = darkMode, onCheckedChange = onDarkModeChange)
            }
        }
    }
}

@Composable
private fun HeroSummary(uiState: DashboardUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(PrimaryGradient())
            .padding(22.dp)
    ) {
        ParticleField(alpha = 0.22f)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Water saved today", color = Color.White.copy(alpha = 0.82f), fontWeight = FontWeight.SemiBold)
            Text(
                "${uiState.todayLitres.roundToInt()} L",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text("Total ${uiState.totalLitres.roundToInt()} L saved", color = Color.White.copy(alpha = 0.82f))
        }
    }
}

@Composable
private fun WaterTankCard(uiState: DashboardUiState) {
    val fill = if (uiState.tankCapacity <= 0) 0f else (uiState.monthlyLitres / uiState.tankCapacity).toFloat().coerceIn(0f, 1f)
    val visualFill = when {
        fill <= 0f -> 0f
        fill < 0.06f -> 0.06f
        else -> fill
    }
    val fillLabel = when {
        fill <= 0f -> "0% filled"
        fill < 0.1f -> String.format("%.1f%% filled", fill * 100f)
        else -> "${(fill * 100).roundToInt()}% filled"
    }
    AppCard {
        SectionBadge("Tank Progress")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WaterTank(visualFill, Modifier.size(width = 130.dp, height = 210.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(fillLabel, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    "${uiState.tankCapacity.roundToInt()} L capacity\n${uiState.roofArea.roundToInt()} sq ft roof\n${uiState.roofMaterial.displayName}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ProgressBar(fill)
            }
        }
    }
}

@Composable
private fun WaterTank(fill: Float, modifier: Modifier) {
    val waveTransition = rememberInfiniteTransition(label = "tankWave")
    val waveShift by waveTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveShift"
    )

    Canvas(modifier = modifier) {
        val stroke = 7.dp.toPx()
        val tankWidth = size.width * 0.72f
        val tankLeft = (size.width - tankWidth) / 2f
        val tankTop = stroke
        val tankHeight = size.height - stroke * 2
        val waterHeight = tankHeight * fill
        val waterTopY = tankTop + tankHeight - waterHeight
        val innerLeft = tankLeft + stroke
        val innerWidth = tankWidth - stroke * 2
        drawRoundRect(
            brush = Brush.verticalGradient(listOf(Color(0xFF93C5FD), Color(0xFF2563EB), Purple)),
            topLeft = Offset(innerLeft, waterTopY),
            size = Size(innerWidth, waterHeight),
            cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
        )
        val wave = Path().apply {
            val baseY = waterTopY + 12.dp.toPx()
            val amplitude = 9.dp.toPx()
            val wavelength = innerWidth / 2f
            var currentX = innerLeft - wavelength + (waveShift * wavelength * 2f)

            moveTo(currentX, baseY)
            while (currentX <= innerLeft + innerWidth + wavelength) {
                quadraticBezierTo(
                    currentX + wavelength / 2f,
                    baseY - amplitude,
                    currentX + wavelength,
                    baseY
                )
                quadraticBezierTo(
                    currentX + wavelength * 1.5f,
                    baseY + amplitude,
                    currentX + wavelength * 2f,
                    baseY
                )
                currentX += wavelength * 2f
            }
        }
        drawPath(wave, Color.White.copy(alpha = 0.72f), style = Stroke(width = 5.dp.toPx()))
        drawRoundRect(
            color = Indigo,
            topLeft = Offset(tankLeft, tankTop),
            size = Size(tankWidth, tankHeight),
            cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
            style = Stroke(width = stroke)
        )
        drawLine(
            color = Purple.copy(alpha = 0.42f),
            start = Offset(tankLeft + tankWidth + 10.dp.toPx(), tankTop + tankHeight * 0.18f),
            end = Offset(tankLeft + tankWidth + 10.dp.toPx(), tankTop + tankHeight),
            strokeWidth = 4.dp.toPx()
        )
    }
}

@Composable
private fun MetricCard(label: String, value: String, icon: String, modifier: Modifier) {
    AppCard {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBubble(icon.take(1).uppercase())
            Column {
                Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
private fun RainfallRow(entry: RainfallLogEntity) {
    AppCard {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")), fontWeight = FontWeight.SemiBold)
                if (!entry.notes.isNullOrBlank()) Text(entry.notes, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(entry.getRainfallString(), fontWeight = FontWeight.Bold)
                Text(entry.getVolumeString(), color = Indigo, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun PrettyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    suffix: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        suffix = if (suffix == null) null else ({ Text(suffix) }),
        keyboardOptions = keyboardOptions,
        minLines = minLines,
        singleLine = minLines == 1,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Indigo,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.28f),
            focusedLabelColor = Purple,
            cursorColor = Pink
        )
    )
}

@Composable
private fun GradientButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(14.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(PrimaryGradient())
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AppCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(14.dp, RoundedCornerShape(28.dp), clip = false)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(Color.White.copy(alpha = 0.70f), Purple.copy(alpha = 0.22f))),
                shape = RoundedCornerShape(28.dp)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun ReportLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
    }
}

@Composable
private fun SectionBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Brush.horizontalGradient(listOf(Indigo.copy(alpha = 0.14f), Pink.copy(alpha = 0.14f))))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, color = Indigo, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun IconBubble(text: String) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(PrimaryGradient()),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(12.dp)
                .clip(CircleShape)
                .background(PrimaryGradient())
        )
    }
}

@Composable
private fun GradientTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun DarkModeRow(darkMode: Boolean, onDarkModeChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Dark mode", fontWeight = FontWeight.SemiBold)
        Switch(checked = darkMode, onCheckedChange = onDarkModeChange)
    }
}

@Composable
private fun EmptyState(text: String) {
    AppCard {
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}

@Composable
private fun MessageText(message: String?) {
    if (!message.isNullOrBlank()) {
        Text(message, color = Pink, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ParticleField(alpha: Float = 0.16f) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val colors = listOf(Indigo, Purple, Pink)
        repeat(16) { index ->
            val x = size.width * (((index * 37) % 100) / 100f)
            val y = size.height * (((index * 53) % 100) / 100f)
            drawCircle(
                color = colors[index % colors.size].copy(alpha = alpha),
                radius = (4 + index % 9).dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
private fun AppBackgroundBrush(): Brush {
    return Brush.verticalGradient(
        listOf(
            if (MaterialTheme.colorScheme.background == Color(0xFF0F172A)) Color(0xFF111827) else SoftLight,
            if (MaterialTheme.colorScheme.background == Color(0xFF0F172A)) Color(0xFF1E1B4B) else SoftSlate
        )
    )
}

private fun PrimaryGradient(): Brush {
    return Brush.horizontalGradient(listOf(Indigo, Purple, Pink))
}
