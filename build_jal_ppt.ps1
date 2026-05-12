$presentationPath = "C:\Users\HP\Documents\Codex\2026-05-05\files-mentioned-by-the-user-room\Jal_Sanchay_Tracker_Presentation.pptx"
$logoPath = "C:\Users\HP\Documents\Codex\2026-05-05\files-mentioned-by-the-user-room\app\src\main\res\drawable\jal_sanchay_logo.png"

function Set-ShapeText {
    param(
        $Slide,
        [string]$ShapeName,
        [string]$Text
    )

    $shape = $Slide.Shapes.Item($ShapeName)
    $shape.TextFrame.TextRange.Text = $Text
}

function Replace-PictureWithLogo {
    param(
        $Slide,
        [string]$ShapeName
    )

    $shape = $Slide.Shapes.Item($ShapeName)
    $left = $shape.Left
    $top = $shape.Top
    $width = $shape.Width
    $height = $shape.Height
    $shape.Delete()
    $Slide.Shapes.AddPicture($logoPath, 0, -1, $left, $top, $width, $height) | Out-Null
}

function Add-TextBox {
    param(
        $Slide,
        [double]$Left,
        [double]$Top,
        [double]$Width,
        [double]$Height,
        [string]$Text,
        [double]$FontSize = 20,
        [bool]$Bold = $false
    )

    $shape = $Slide.Shapes.AddTextbox(1, $Left, $Top, $Width, $Height)
    $shape.TextFrame.TextRange.Text = $Text
    $shape.TextFrame.TextRange.Font.Size = $FontSize
    $shape.TextFrame.TextRange.Font.Bold = $(if ($Bold) { -1 } else { 0 })
    return $shape
}

function Add-InfoBox {
    param(
        $Slide,
        [double]$Left,
        [double]$Top,
        [double]$Width,
        [double]$Height,
        [string]$Text
    )

    $shape = $Slide.Shapes.AddShape(1, $Left, $Top, $Width, $Height)
    $shape.TextFrame.TextRange.Text = $Text
    $shape.Fill.ForeColor.RGB = 16777215
    $shape.Line.ForeColor.RGB = 9145227
    $shape.TextFrame.TextRange.Font.Size = 18
    $shape.TextFrame.TextRange.Font.Bold = -1
    return $shape
}

$ppt = New-Object -ComObject PowerPoint.Application
$presentation = $ppt.Presentations.Open($presentationPath, $false, $false, $false)

# Slide 1 - Cover
$slide = $presentation.Slides.Item(1)
Replace-PictureWithLogo -Slide $slide -ShapeName "object 4"
Set-ShapeText -Slide $slide -ShapeName "TextBox 16" -Text "         Project Guide`r        ( Update Name )"
Set-ShapeText -Slide $slide -ShapeName "TextBox 18" -Text "Update Student 1 (USN)`rUpdate Student 2 (USN)`rUpdate Student 3 (USN)`rUpdate Student 4 (USN)"
Set-ShapeText -Slide $slide -ShapeName "TextBox 2" -Text "                            MINI PROJECT`r               ""JAL-SANCHAY TRACKER"""

# Slide 2 - Contents
$slide = $presentation.Slides.Item(2)
Set-ShapeText -Slide $slide -ShapeName "Title 10" -Text "Contents:"
Set-ShapeText -Slide $slide -ShapeName "TextBox 12" -Text "Abstract`rAim`rIntroduction`rProblem Statement`rObjectives`rFeatures`rLiterature Survey`rDatabase Design`rSoftware Requirements`rHardware Requirements`rScreens`rConclusion`rReferences"

# Slide 3 - Abstract
$slide = $presentation.Slides.Item(3)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "Abstract:"
Set-ShapeText -Slide $slide -ShapeName "Content Placeholder 2" -Text "Jal-Sanchay Tracker is an Android application developed to help households measure the effectiveness of rainwater harvesting using a simple guided flow. The app is built with Kotlin and Jetpack Compose for the user interface, Room Database for local storage, Hilt for dependency injection, and MVVM for clean architecture. Users first complete a household setup, then enter rainfall values in millimeters. Based on the roof area, tank capacity, and roof material, the app estimates harvested water and presents the result through a live dashboard, tank progress visual, report, impact view, and water-saving tips. The application stores historical rainfall entries locally and updates the dashboard in real time using Room Flow, StateFlow, and Compose recomposition."

# Slide 4 - Aim
$slide = $presentation.Slides.Item(4)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "Aim:"
Set-ShapeText -Slide $slide -ShapeName "Content Placeholder 2" -Text "To design and implement a user-friendly Android application that helps households record rainfall, estimate harvested water, track storage progress, and understand the practical impact of rainwater conservation."
Set-ShapeText -Slide $slide -ShapeName "Rectangle 5" -Text "Guided Android flow`rModern Compose UI`rRainfall data validation`rWater saving calculation`rRoom database history`rDashboard and impact tracking"

# Slide 5 - Introduction
$slide = $presentation.Slides.Item(5)
Set-ShapeText -Slide $slide -ShapeName "TextBox 6" -Text "INTRODUCTION:"
Set-ShapeText -Slide $slide -ShapeName "TextBox 2" -Text "Jal-Sanchay Tracker is a sustainability-focused Android application created to make rainwater harvesting measurable and understandable for everyday users. The app provides a guided journey from setup to rainfall entry, dashboard monitoring, reporting, impact calculation, and practical tips. It is developed using Kotlin, Jetpack Compose, Room Database, Hilt, Coroutines, and MVVM architecture. By storing setup and rainfall data locally and reacting to changes in real time, the app helps users see how much water has been saved, how full their storage tank is, and how many household water days that saving represents. This improves awareness, encourages conservation habits, and makes rainwater harvesting more actionable."

# Slide 6 - Problem Statement
$slide = $presentation.Slides.Item(6)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "Problem Statement:"
Set-ShapeText -Slide $slide -ShapeName "Content Placeholder 2" -Text "Many households have rainwater harvesting systems but no simple way to measure whether they are actually effective. Without a clear record of rainfall, roof catchment, and stored water, conservation stays conceptual instead of practical. Manual tracking is inconsistent, calculations are often skipped, and users rarely understand how rainfall translates into daily household impact. In addition, most users need a clean and easy interface rather than a technical system. A mobile application is therefore required to store historical rainfall data, estimate water savings automatically, and present the result through clear visuals such as tank progress, reports, and impact views."

# Slide 7 - Objectives
$slide = $presentation.Slides.Item(7)
Set-ShapeText -Slide $slide -ShapeName "TextBox 3" -Text "OBJECTIVES:"
Set-ShapeText -Slide $slide -ShapeName "Rectangle 2" -Text "Simple Setup`rAllow the user to save household details like roof area and tank capacity.`r`rRainfall Entry`rRecord rainfall values with validation and local storage.`r`rLive Dashboard`rShow water saved, recent entries, and animated tank progress.`r`rMonthly Report`rSummarize total and monthly harvested water clearly.`r`rImpact Score`rConvert litres into understandable household water days.`r`rUser Guidance`rProvide practical tips for better rainwater harvesting."

# Slide 8 - Features
$slide = $presentation.Slides.Item(8)
Set-ShapeText -Slide $slide -ShapeName "TextBox 2" -Text "FEATURES:"
Set-ShapeText -Slide $slide -ShapeName "TextBox 5" -Text "The application is designed to reduce manual effort and provide meaningful household water insights through a guided Android experience. Major features include:"
Set-ShapeText -Slide $slide -ShapeName "Rectangle 2" -Text "Welcome Screen`rBranded entry screen for a guided user experience.`r`rHousehold Setup`rStores name, roof area, tank capacity, and roof material.`r`rRainfall Calculation`rConverts rainfall into estimated saved water.`r`rRoom Database`rStores historical entries locally on the device.`r`rLive Dashboard`rUpdates tank progress and summaries in real time.`r`rReport and Impact`rShows monthly totals and household water-day impact.`r`rTips Section`rEncourages better conservation practices."

# Slide 9 - Literature Survey table
$slide = $presentation.Slides.Item(9)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "Literature Survey:"
$table = $slide.Shapes.Item(2).Table
$literature = @(
    @("No", "Title (Short)", "Author(s)", "Year", "Key Focus", "Conclusion"),
    @("1", "Urban Rainwater Harvesting", "A. Campisano et al.", "2017", "Domestic rainwater reuse systems", "Monitoring and storage design improve household water efficiency"),
    @("2", "Rainwater Quality and Use", "M. S. Islam et al.", "2010", "Usability of rooftop-harvested rainwater", "Proper collection and storage make rainwater a valuable local resource"),
    @("3", "Smart Water Monitoring", "R. K. Kodali et al.", "2018", "IoT-based water level and usage tracking", "Real-time tracking increases awareness and better decision-making"),
    @("4", "Sustainable Urban Water Systems", "M. Sharma, P. Kansal", "2013", "Rainwater harvesting in Indian cities", "Local harvesting supports sustainability and reduces dependency on municipal supply"),
    @("5", "Decision Support for Harvesting", "J. Liaw, Y. Tsai", "2004", "Storage sizing and rainfall data use", "Historical rainfall analysis is essential for effective harvesting systems"),
    @("6", "Household Conservation Behaviour", "S. Willis et al.", "2011", "How feedback changes water use", "Visible usage feedback encourages long-term conservation habits")
)
for ($r = 1; $r -le $table.Rows.Count; $r++) {
    for ($c = 1; $c -le $table.Columns.Count; $c++) {
        $table.Cell($r, $c).Shape.TextFrame.TextRange.Text = $literature[$r - 1][$c - 1]
    }
}

# Slide 10 - Database Design
$slide = $presentation.Slides.Item(10)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "Database Design:"
$slide.Shapes.Item(3).Delete()
$slide.Shapes.Item(2).Delete()
$box1 = Add-InfoBox -Slide $slide -Left 70 -Top 180 -Width 230 -Height 120 -Text "user_infrastructure`rName / user id`rRoof area`rTank capacity`rRoof material"
$box2 = Add-InfoBox -Slide $slide -Left 360 -Top 180 -Width 230 -Height 120 -Text "rainfall_log`rRainfall (mm)`rSaved litres`rTimestamp`rHistorical entries"
$box3 = Add-InfoBox -Slide $slide -Left 650 -Top 180 -Width 230 -Height 120 -Text "monthly_report_cache`rMonthly totals`rCached reporting values"
$box4 = Add-InfoBox -Slide $slide -Left 940 -Top 180 -Width 230 -Height 120 -Text "app_settings`rTheme / settings`rApp-level preferences"
$desc = Add-TextBox -Slide $slide -Left 90 -Top 350 -Width 980 -Height 180 -Text "Room Database is used for local persistence in the application. The `user_infrastructure` table stores household setup details, while `rainfall_log` stores each rainfall entry as a historical record. Monthly summaries are supported through `monthly_report_cache`, and internal preferences can be stored in `app_settings`. DAO queries expose Flow-based streams so the dashboard updates automatically when new rainfall data is inserted." -FontSize 20

# Slide 11 - Software Requirements
$slide = $presentation.Slides.Item(11)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "System Requirements:"
Set-ShapeText -Slide $slide -ShapeName "Content Placeholder 2" -Text "Software Requirements`r1. Programming Language:`rKotlin`r`r2. UI Toolkit:`rJetpack Compose, Material 3`r`r3. Database:`rRoom Database (SQLite)`r`r4. Architecture / Libraries:`rHilt, Coroutines, StateFlow, MVVM`r`r5. Development Tools:`rAndroid Studio, Gradle, Android SDK"

# Slide 12 - Hardware Requirements
$slide = $presentation.Slides.Item(12)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "System Requirements: Hardware Requirements"
Set-ShapeText -Slide $slide -ShapeName "Content Placeholder 2" -Text "Developer System:`rProcessor: Intel i3 / i5 / Ryzen equivalent or above`rRAM: Minimum 8 GB recommended`rStorage: At least 2 GB free for Android Studio, SDK, and project files`r`rAndroid Test Device:`rAndroid version: 7.0 (API 24) or above`rRAM: 3 GB or higher recommended`rStorage: Minimum 200 MB free for app install and local database`rDisplay: Standard phone screen for testing responsive Compose UI"

# Slide 13 - Screens
$slide = $presentation.Slides.Item(13)
Set-ShapeText -Slide $slide -ShapeName "TextBox 9" -Text "Welcome Page"
Set-ShapeText -Slide $slide -ShapeName "TextBox 11" -Text "Setup Page"
Set-ShapeText -Slide $slide -ShapeName "TextBox 13" -Text "Dashboard / Report"
Set-ShapeText -Slide $slide -ShapeName "TextBox 15" -Text "Impact / Tips"
Replace-PictureWithLogo -Slide $slide -ShapeName "Picture 2"
Replace-PictureWithLogo -Slide $slide -ShapeName "Picture 4"
Replace-PictureWithLogo -Slide $slide -ShapeName "Picture 6"
Replace-PictureWithLogo -Slide $slide -ShapeName "Picture 8"

# Slide 14 - Conclusion
$slide = $presentation.Slides.Item(14)
Set-ShapeText -Slide $slide -ShapeName "Title 1" -Text "CONCLUSION:"
Set-ShapeText -Slide $slide -ShapeName "Text Placeholder 2" -Text "Jal-Sanchay Tracker successfully demonstrates how an Android application can turn rainwater harvesting into a measurable and user-friendly household activity. By combining setup data, rainfall entry, local Room storage, and a reactive Jetpack Compose dashboard, the system gives users clear feedback about litres saved, tank progress, and household impact. The project also highlights good Android development practices through MVVM architecture, Hilt dependency injection, validation logic, and real-time state updates using Flow and StateFlow. Overall, the application promotes sustainability awareness while serving as a strong internship-level implementation of Kotlin Android development."

# Slide 15 - References
$slide = $presentation.Slides.Item(15)
while ($slide.Shapes.Count -gt 0) {
    $slide.Shapes.Item(1).Delete()
}
$title = Add-TextBox -Slide $slide -Left 70 -Top 50 -Width 500 -Height 50 -Text "REFERENCES:" -FontSize 28 -Bold $true
$body = Add-TextBox -Slide $slide -Left 90 -Top 130 -Width 1000 -Height 400 -Text "1. Android Developers Documentation - Jetpack Compose`r2. Android Developers Documentation - Room Persistence Library`r3. Android Developers Documentation - Hilt and dependency injection`r4. Research articles on rainwater harvesting and household water conservation`r5. Project requirement brief for Jal-Sanchay Tracker" -FontSize 22

$presentation.Save()
$presentation.Close()
$ppt.Quit()
