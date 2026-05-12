$workspace = "C:\Users\HP\Documents\Codex\2026-05-05\files-mentioned-by-the-user-room"
$referencePpt = Join-Path $workspace "Kashta_Kala_reference.pptx"
$outputPpt = Join-Path $workspace "Jal_Sanchay_Beautiful_Presentation.pptx"
$logoPath = Join-Path $workspace "app\src\main\res\drawable\jal_sanchay_logo.png"

$appShots = @(
    (Join-Path $workspace "app6.jpeg"),
    (Join-Path $workspace "app5.jpeg"),
    (Join-Path $workspace "app4.jpeg"),
    (Join-Path $workspace "app3.jpeg"),
    (Join-Path $workspace "app2.jpeg"),
    (Join-Path $workspace "app1.jpeg")
)

Copy-Item $referencePpt $outputPpt -Force

function Set-Text {
    param(
        $slide,
        [string]$shapeName,
        [string]$text
    )
    try {
        $slide.Shapes.Item($shapeName).TextFrame.TextRange.Text = $text
    } catch {
        Write-Output "Shape $shapeName not found on slide $($slide.SlideIndex)"
    }
}

function Add-FittedPicture {
    param(
        $slide,
        [string]$file,
        [double]$left,
        [double]$top,
        [double]$width,
        [double]$height
    )

    Add-Type -AssemblyName System.Drawing
    $img = [System.Drawing.Image]::FromFile($file)
    $ratio = $img.Width / $img.Height
    $boxRatio = $width / $height
    if ($ratio -gt $boxRatio) {
        $picWidth = $width
        $picHeight = $width / $ratio
        $picLeft = $left
        $picTop = $top + (($height - $picHeight) / 2)
    } else {
        $picHeight = $height
        $picWidth = $height * $ratio
        $picTop = $top
        $picLeft = $left + (($width - $picWidth) / 2)
    }
    $img.Dispose()
    $slide.Shapes.AddPicture($file, 0, -1, $picLeft, $picTop, $picWidth, $picHeight) | Out-Null
}

function Add-ScreenshotCard {
    param(
        $slide,
        [string]$imagePath,
        [string]$caption,
        [double]$left,
        [double]$top,
        [double]$cardWidth,
        [double]$cardHeight
    )

    $card = $slide.Shapes.AddShape(5, $left, $top, $cardWidth, $cardHeight)
    $card.Fill.ForeColor.RGB = 16777215
    $card.Line.ForeColor.RGB = 14540253
    $card.Line.Weight = 1.25

    $imageTop = $top + 10
    $imageHeight = $cardHeight - 42
    Add-FittedPicture -slide $slide -file $imagePath -left ($left + 8) -top $imageTop -width ($cardWidth - 16) -height $imageHeight

    $captionBox = $slide.Shapes.AddTextbox(1, $left + 8, $top + $cardHeight - 28, $cardWidth - 16, 20)
    $captionBox.TextFrame.TextRange.Text = $caption
    $captionBox.TextFrame.TextRange.Font.Bold = -1
    $captionBox.TextFrame.TextRange.Font.Size = 13
    $captionBox.TextFrame.TextRange.ParagraphFormat.Alignment = 2
}

function Remove-ShapeRange {
    param(
        $slide,
        [int]$startIndex,
        [int]$endIndex
    )
    for ($i = $endIndex; $i -ge $startIndex; $i--) {
        $slide.Shapes.Item($i).Delete()
    }
}

$ppt = New-Object -ComObject PowerPoint.Application
$presentation = $ppt.Presentations.Open($outputPpt, $false, $false, $false)

# Slide 1 - Cover
$slide = $presentation.Slides.Item(1)
Set-Text $slide "Text 0" "Internship Phase - 2 Presentation`r(22INT)"
Set-Text $slide "Text 1" "Internship Guide`rUpdate Guide Name`rAssociate Professor`rDepartment of CSE`rMCE, Hassan"
Set-Text $slide "TextBox 3" "Internship Title: Android App Development using Gen AI`rProject: Jal-Sanchay Tracker`rCompany: MindMatrix`rName: Update Student Name`rUSN: Update USN`rBranch: Computer Science & Engineering`rCourse: Android App Development using Gen AI"
$slide.Shapes.AddPicture($logoPath, 0, -1, 525, 80, 120, 120) | Out-Null

# Slide 2 - Project intro
$slide = $presentation.Slides.Item(2)
Set-Text $slide "Text 10" "MindMatrix VTU"
Set-Text $slide "Text 11" "PROJECT #86"
Set-Text $slide "Text 12" "Jal-Sanchay"
Set-Text $slide "Text 13" "Rainwater Tracker`rfor Households"
Set-Text $slide "Text 15" "Android App Development using GenAI`rNatural Resources Track"
Set-Text $slide "Text 16" "MindMatrix VTU Internship Program"
$slide.Shapes.AddPicture($logoPath, 0, -1, 55, 110, 170, 170) | Out-Null

# Slide 3 - Problem
$slide = $presentation.Slides.Item(3)
Set-Text $slide "Text 1" "THE PROBLEM"
Set-Text $slide "Text 2" "Why Water Saving Stays Invisible"
Set-Text $slide "Text 5" "1"
Set-Text $slide "Text 6" "No Measurement"
Set-Text $slide "Text 7" "Many households collect rainwater but do not know how much water is actually saved after each rainfall."
Set-Text $slide "Text 10" "2"
Set-Text $slide "Text 11" "No Historical Record"
Set-Text $slide "Text 12" "Rainfall is rarely stored in a structured way, so monthly and all-time conservation becomes hard to review."
Set-Text $slide "Text 15" "3"
Set-Text $slide "Text 16" "Low Awareness"
Set-Text $slide "Text 17" "Without a clear dashboard, tank progress, and impact score, conservation remains hard to explain or improve."

# Slide 4 - Vision
$slide = $presentation.Slides.Item(4)
Set-Text $slide "Text 1" "THE VISION"
Set-Text $slide "Text 2" "Jal-Sanchay"
Set-Text $slide "Text 3" "A Household Rainwater App`rfor Everyday Tracking"
Set-Text $slide "Text 5" "Jal-Sanchay Tracker is a mobile application that helps households save setup details, record rainfall, estimate harvested water, and understand the real-life impact of conservation through a clean guided flow."
Set-Text $slide "Text 6" "Core Value"
Set-Text $slide "Text 7" "1"
Set-Text $slide "Text 8" "Save Setup"
Set-Text $slide "Text 9" "Roof area, tank capacity, and roof material"
Set-Text $slide "Text 11" "2"
Set-Text $slide "Text 12" "Enter Rainfall"
Set-Text $slide "Text 13" "Record rainfall values with validation"
Set-Text $slide "Text 15" "3"
Set-Text $slide "Text 16" "Track Dashboard"
Set-Text $slide "Text 17" "See saved litres, tank progress, and history"
Set-Text $slide "Text 19" "4"
Set-Text $slide "Text 20" "Measure Impact"
Set-Text $slide "Text 21" "Convert litres into household water-day meaning"

# Slide 5 - Screens part 1
$slide = $presentation.Slides.Item(5)
Set-Text $slide "Text 1" "APP SCREENS"
Set-Text $slide "Text 2" "Guided User Flow - Part 1"
Remove-ShapeRange $slide 4 27
Add-ScreenshotCard -slide $slide -imagePath $appShots[0] -caption "Welcome Screen" -left 28 -top 112 -cardWidth 208 -cardHeight 250
Add-ScreenshotCard -slide $slide -imagePath $appShots[1] -caption "Setup Screen" -left 256 -top 112 -cardWidth 208 -cardHeight 250
Add-ScreenshotCard -slide $slide -imagePath $appShots[2] -caption "Data Entry Screen" -left 484 -top 112 -cardWidth 208 -cardHeight 250

# Slide 6 - Screens part 2
$slide = $presentation.Slides.Item(6)
Set-Text $slide "Text 1" "APP SCREENS"
Set-Text $slide "Text 2" "Guided User Flow - Part 2"
Remove-ShapeRange $slide 4 37
Add-ScreenshotCard -slide $slide -imagePath $appShots[3] -caption "Dashboard" -left 28 -top 112 -cardWidth 208 -cardHeight 250
Add-ScreenshotCard -slide $slide -imagePath $appShots[4] -caption "Report" -left 256 -top 112 -cardWidth 208 -cardHeight 250
Add-ScreenshotCard -slide $slide -imagePath $appShots[5] -caption "Impact Score" -left 484 -top 112 -cardWidth 208 -cardHeight 250

# Slide 7 - Technical implementation
$slide = $presentation.Slides.Item(7)
Set-Text $slide "Text 0" "TECHNICAL IMPLEMENTATION"
Set-Text $slide "Text 1" "How We Build It"
Set-Text $slide "Text 4" "Android (Kotlin + Compose)"
Set-Text $slide "Text 5" "-> Single-activity flow with polished Compose screens"
Set-Text $slide "Text 6" "-> Guided navigation from Welcome to Tips"
Set-Text $slide "Text 7" "-> Reusable UI cards, buttons, and tank visual"
Set-Text $slide "Text 10" "Room Database"
Set-Text $slide "Text 11" "-> Store user_infrastructure and rainfall_log tables"
Set-Text $slide "Text 12" "-> Save historical rainfall entries locally"
Set-Text $slide "Text 13" "-> Support monthly report and offline persistence"
Set-Text $slide "Text 16" "Reactive State Updates"
Set-Text $slide "Text 17" "-> DAO queries expose Flow for live database changes"
Set-Text $slide "Text 18" "-> ViewModel uses StateFlow for screen state"
Set-Text $slide "Text 19" "-> Compose recomposes dashboard and tank in real time"
Set-Text $slide "Text 22" "Water Calculation Logic"
Set-Text $slide "Text 23" "-> Rainfall, roof area, and runoff behavior estimate saved litres"
Set-Text $slide "Text 24" "-> Tank percentage and household days are derived values"
Set-Text $slide "Text 25" "-> Impact screen explains the conservation result clearly"

# Slide 8 - Impact goals
$slide = $presentation.Slides.Item(8)
Set-Text $slide "Text 1" "IMPACT GOALS"
Set-Text $slide "Text 2" "Why This Matters"
Set-Text $slide "Text 6" "1"
Set-Text $slide "Text 7" "Water Awareness"
Set-Text $slide "Text 8" "Makes Saving Visible"
Set-Text $slide "Text 9" "The app turns rainfall into visible litres, tank progress, and report data that users can understand quickly."
Set-Text $slide "Text 13" "2"
Set-Text $slide "Text 14" "Household Tracking"
Set-Text $slide "Text 15" "Builds Better Habits"
Set-Text $slide "Text 16" "Historical entries and monthly summaries help families review how much rainwater harvesting is really contributing."
Set-Text $slide "Text 20" "3"
Set-Text $slide "Text 21" "Concept Clarity"
Set-Text $slide "Text 22" "Explains Impact"
Set-Text $slide "Text 23" "Impact score converts saved litres into household water days, making conservation easier to explain in demos and reports."
Set-Text $slide "Text 27" "4"
Set-Text $slide "Text 28" "Sustainability"
Set-Text $slide "Text 29" "Encourages Action"
Set-Text $slide "Text 30" "A clean and friendly mobile experience motivates regular entry of rainfall data and stronger long-term water awareness."

# Slide 9 - Success criteria / conclusion
$slide = $presentation.Slides.Item(9)
Set-Text $slide "Text 1" "SUCCESS CRITERIA"
Set-Text $slide "Text 2" "What Makes Jal-Sanchay`rDemo Ready"
Set-Text $slide "Text 4" "OK  Guided flow: Welcome -> Setup -> Data Entry -> Dashboard -> Report -> Impact -> Tips"
Set-Text $slide "Text 6" "OK  Rainfall history stored and retrieved through Room Database"
Set-Text $slide "Text 8" "OK  Dashboard updates live using Flow, StateFlow, and Compose recomposition"
Set-Text $slide "Text 10" "OK  Water tank progress and household impact shown visually"
Set-Text $slide "Text 12" "OK  Clean branded UI using the blue and aqua app theme"
Set-Text $slide "Text 14" "OK  Ready for internship presentation and database demonstration"
Set-Text $slide "Text 15" "Future Scope"
Set-Text $slide "Text 16" "Live weather API integration"
Set-Text $slide "Text 17" "Charts for rainfall analytics"
Set-Text $slide "Text 18" "Cloud sync and backup"
Set-Text $slide "Text 19" "Regional language support"
Set-Text $slide "Text 20" "Smart reminders for rainfall entry"
Set-Text $slide "Text 22" "Measure every drop.`rExplain every saving."

$presentation.Save()
$presentation.Close()
$ppt.Quit()
