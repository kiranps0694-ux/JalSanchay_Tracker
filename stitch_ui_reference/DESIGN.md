---
name: Hydro-Vibrance
colors:
  surface: '#f9f9ff'
  surface-dim: '#d4daea'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f1f3ff'
  surface-container: '#e8eeff'
  surface-container-high: '#e3e8f9'
  surface-container-highest: '#dde2f3'
  on-surface: '#161c27'
  on-surface-variant: '#424654'
  inverse-surface: '#2a303d'
  inverse-on-surface: '#ecf0ff'
  outline: '#737785'
  outline-variant: '#c3c6d6'
  surface-tint: '#0056d2'
  primary: '#0040a1'
  on-primary: '#ffffff'
  primary-container: '#0056d2'
  on-primary-container: '#ccd8ff'
  inverse-primary: '#b2c5ff'
  secondary: '#00696e'
  on-secondary: '#ffffff'
  secondary-container: '#00f4fe'
  on-secondary-container: '#006c71'
  tertiary: '#005228'
  on-tertiary: '#ffffff'
  tertiary-container: '#006d37'
  on-tertiary-container: '#5ff393'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dae2ff'
  primary-fixed-dim: '#b2c5ff'
  on-primary-fixed: '#001847'
  on-primary-fixed-variant: '#0040a1'
  secondary-fixed: '#63f7ff'
  secondary-fixed-dim: '#00dce5'
  on-secondary-fixed: '#002021'
  on-secondary-fixed-variant: '#004f53'
  tertiary-fixed: '#6bfe9c'
  tertiary-fixed-dim: '#4ae183'
  on-tertiary-fixed: '#00210c'
  on-tertiary-fixed-variant: '#005228'
  background: '#f9f9ff'
  on-background: '#161c27'
  surface-variant: '#dde2f3'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 20px
  margin-mobile: 16px
  margin-desktop: 40px
---

## Brand & Style

The visual identity of the design system is anchored in the concept of "Vitality through Conservation." It balances the technical precision of a tracking utility with an organic, inviting aesthetic that mirrors the fluidity of water. The brand personality is optimistic, proactive, and transparent, aimed at eco-conscious citizens and households.

The design style is a hybrid of **Minimalism** and **Glassmorphism**. It utilizes heavy whitespace to allow vibrant data visualizations to breathe, while employing translucent, frosted layers to create a "liquid" depth. This approach moves away from the static nature of traditional SaaS and towards a dynamic, immersive experience that feels as refreshing as the resource it seeks to protect.

## Colors

The color palette is a high-contrast symphony of aquatic tones and sustainability markers. 
- **Deep Sea Blue (Primary):** Used for core navigation, primary actions, and branding. It represents depth, reliability, and the vastness of water resources.
- **Electric Aqua (Secondary):** A high-vibrancy accent used for interactive states, highlights, and secondary information. It provides a refreshing, glowing contrast against the primary blue.
- **Growth Green (Tertiary):** Dedicated specifically to sustainability metrics, "Water Wealth" indicators, and successful conservation goals. 
- **Neutral / Background:** A clean, slightly cool-tinted white base ensures that vibrant accents pop, maintaining high legibility and a crisp "modern lab" feel.

## Typography

This design system utilizes **Plus Jakarta Sans** for its entire type scale. The font was selected for its friendly, geometric clarity and contemporary feel. Its slightly rounded terminals complement the "liquid" design language, while its generous x-height ensures excellent readability during data entry and dashboard monitoring.

Headlines are set with tight tracking and bold weights to command attention in a high-contrast environment. Body copy maintains a spacious line height to evoke a sense of calm and openness, essential for an app focused on resource management.

## Layout & Spacing

The layout philosophy is built on an **8px grid system**, ensuring mathematical harmony across all components. The design system utilizes a **fluid grid** model that prioritizes mobile-first accessibility. 

Vertical spacing is intentionally generous to prevent the vibrant color palette from feeling overwhelming. Use `lg` (24px) or `xl` (32px) spacing between distinct content sections to maintain the "Minimalist" clarity. Gutters are kept wide to ensure that glassmorphic cards do not visually bleed into one another, preserving the "floating" effect.

## Elevation & Depth

Hierarchy is achieved through **Glassmorphism** and **Ambient Shadows**. Instead of traditional solid grey shadows, this design system uses "Tinted Shadows"—low-opacity shadows that take on a hint of the primary or secondary color (e.g., a 10% opacity Deep Sea Blue shadow).

Depth is layered as follows:
1.  **Canvas (Level 0):** The base background.
2.  **Translucent Cards (Level 1):** Surfaces with a `backdrop-filter: blur(20px)` and a 70% white opacity. These cards should have a subtle 1px inner border (white, 20% opacity) to simulate the edge of a glass container.
3.  **Active Elements (Level 2):** Primary buttons and active states that use vibrant gradients and soft, diffused shadows to appear "lifted" above the glass surfaces.

## Shapes

The shape language is consistently **Rounded (Level 2)**. This choice strikes the perfect balance between professional utility and organic approachability. 

- **Standard Elements:** 0.5rem (8px) radius for input fields and small buttons.
- **Large Components:** 1rem (16px) radius for cards and containers, reinforcing the soft, friendly aesthetic.
- **Data Visuals:** Circular and elliptical shapes are preferred for progress indicators (e.g., a "filling water tank" visual) to mimic natural liquid containers.

## Components

### Buttons
Primary buttons should use a vertical gradient from Secondary Aqua to Primary Blue. They feature a high-contrast white label and a "glow" shadow effect. Secondary buttons should be glassmorphic with a Primary Blue outline.

### Cards
Cards are the heart of the dashboard. They must feature the frosted glass effect with high corner radius (16px). Information within cards should be segmented by subtle horizontal dividers (1px, low-contrast aqua).

### Data Visualization
Charts should avoid sharp angles. Line charts should use "smooth" Bézier curves. Progress bars should be designed as "Tank Indicators"—vertical or circular containers that visually "fill" with vibrant blue as the user saves more water.

### Input Fields
Inputs are clean with a Soft (8px) roundedness. The focus state should be marked by an Electric Aqua glow (outer shadow) and a 2px Primary Blue border.

### Chips & Badges
Used for categories like "Rainwater," "Greywater," or "Tips." These use high-saturation background tints with dark text to maintain the vibrant feel without sacrificing accessibility.