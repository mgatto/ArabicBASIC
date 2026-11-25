# ArabicBASIC GitHub Pages — Editorial Site Plan

**Status:** Plan only. The site has not been implemented.

## Purpose

Create a small, editorial GitHub Pages site that explains **why ArabicBASIC exists** and **what its originator hopes it can become**. It should introduce the project as a working interpreter and as an inquiry into programming-language design, language, and access.

This is not a second README or a documentation portal. The site has one project destination: the repository's [`README.md`](https://github.com/mgatto/ArabicBASIC/blob/main/README.md). The essay itself supplies the context; readers who want project details continue to the README.

## Editorial position

### Central idea

Programming should not require translating yourself before you can begin thinking about a problem. ArabicBASIC asks what it means to make Arabic part of the programming language itself—not only the interface, tutorial, or explanation around code.

The argument should be careful and invitational:

- English-first conventions can add a linguistic step for people who do not use English as their primary language.
- A programming language expressed in Arabic is one way to make a different relationship between language and computing possible.
- ArabicBASIC is an experiment and a working system, not proof that programming in Arabic is inherently easier or that language alone determines who can become a programmer.
- The aim is to expand the available choices for learning and programming, not to replace English or other programming languages.

### Why ArabicBASIC?

ArabicBASIC makes the question concrete. It is a BASIC-style interpreter whose programs use Arabic keywords and identifiers, and which accepts Eastern Arabic–Indic numerals. The design work reaches beyond replacing English words: Arabic script, right-to-left text, mixed-direction code, orthographic variation, and the editor experience all become engineering and language-design concerns.

BASIC is a useful setting for the experiment because its compact, recognizable structure keeps language choices visible. It gives the project room to ask how programming constructs can be expressed in Arabic without implying that the current interpreter is a complete curriculum or a replacement for general-purpose languages.

### What I want for its future

Present these as hopes and research directions, not as features that already exist or a fixed product roadmap:

1. **Ground language choices in evidence.** Develop corpus-informed reasoning for keyword choices and investigate how register, diglossia, and orthographic variation affect those choices.
2. **Make the project more learnable in Arabic.** Grow Arabic-language learning materials, examples, and teaching resources so Arabic is present in the learning experience as well as the program syntax.
3. **Learn from people using it.** Seek feedback from Arabic speakers, learners, educators, and language designers; evaluate where the language and tools help and where they still create friction.
4. **Keep improving the full programming experience.** Continue to attend to right-to-left editing, mixed-direction text, diagnostics, installation, and other practical details that determine whether the language is usable day to day.
5. **Explore a broader possibility.** In the longer term, investigate whether ideas from ArabicBASIC could support experiments with programming languages shaped around other linguistic communities and local needs. This is an aspiration, not a capability currently offered by the interpreter.

## Audience and voice

The page is English-first. English is the canonical copy for this version so a professional translator can produce the Arabic edition later.

Use a **mixed voice** deliberately:

- Use **first person** for personal motivation, the origin of the question, and hopes for the future: “I started asking…”, “I want…”, “I hope…”.
- Use the **project voice** for concrete present-tense description: “ArabicBASIC is a BASIC-style interpreter…”, “The editor supports…”.

Keep the English copy translation-ready:

- Use semantic headings and straightforward paragraphs; avoid text embedded in graphics.
- Keep each idea and section self-contained so it can be translated without reconstructing the argument from visual layout.
- Feature Arabic as real text, not an image. Mark Arabic examples with the appropriate language and right-to-left direction in the eventual HTML.
- Do not add automatic translation or a language switch in this first version. A professionally translated Arabic edition can be added later.

## Suggested one-page structure

### 1. Hero — the question

**Working headline:** “What if programming spoke Arabic?”

Use an English-first lead with a prominent Arabic phrase from the existing project identity:

> **برمجة آمنة بلغتك الأم**

The English lead should frame ArabicBASIC as an experiment in making Arabic the language of code, rather than only the language used to explain code. The Arabic phrase is a featured part of the composition, not copy baked into a logo or image.

### 2. Why this question matters

Introduce the extra effort that can arise when learning and programming tools assume English. Avoid presenting English as a problem in itself or making unsupported claims about learning outcomes. The point is that language can be a design choice, and that choice affects who gets to engage with programming on their own terms.

### 3. Why Arabic, and why BASIC?

Explain the project’s design setting: Arabic offers a meaningful case for exploring programming-language localization, while BASIC’s small, recognizable structure makes design decisions easy to see. Keep the focus on the experiment, not a comparison claiming Arabic is superior to other languages.

### 4. Not just translated keywords

Show a short, accurate ArabicBASIC example as live text, for example:

```basic
صار ن = ٥
اذا ن > ٣ ثم
    اطبع "أهلاً"
ختام اذا
```

Use the example to lead into concrete design questions: how right-to-left text behaves alongside operators and digits, how Arabic identifiers work, and how language and orthography influence keyword design. Ensure the eventual layout preserves the code's logical character order and remains copyable.

### 5. What exists now

Describe only the current interpreter and desktop experience, using the README and current project documentation as the factual sources. Keep this section concise; it establishes that ArabicBASIC is a working project rather than a speculative proposal.

### 6. What I hope comes next

Return to first person and introduce the future directions above as questions and intentions. Distinguish current work from longer-term ambitions, especially the possible wider platform for multilingual programming-language experiments.

### 7. One destination

End with one clear call to action to **read the repository README**. Do not link to releases, documentation pages, examples, papers, social accounts, or external project destinations. The README is the only outbound project link.

## Visual direction

### Art direction

Use an **editorial print / research-notebook** feel rather than a software-product landing page:

- Strong typographic hierarchy and generous margins.
- A prominent Arabic headline or pull-quote paired with English editorial copy.
- Narrow, readable text measure and restrained marginal notes or section markers.
- A carefully typeset Arabic code specimen as the main visual evidence.
- If an image is used, prefer genuine ArabicBASIC code or editor imagery. Avoid generic technology stock imagery, cultural clichés, and visual assets from the commercial site.

### Distinct palette for ArabicBASIC

Use the approved **print-editorial palette**:

| Role | Color | Use |
|---|---|---|
| Pale lilac paper | `#F7F4F8` | Main page background |
| Plum-charcoal ink | `#211B26` | Body text and headings |
| Pomegranate | `#B83E58` | Primary accent, rules, selected emphasis |
| Lapis-violet | `#5146B8` | Secondary accent, links, labels |
| Citron | `#C3C94C` | Small decorative highlight only; not small text |
| Dark code panel | `#1D1A22` | Optional code specimen background |

Use the plum-charcoal for body text. Check contrast for all text and interactive states; do not rely on color alone to identify links or meaning. Keep citron to decoration because it is not suitable for small text on the pale background.

### Deliberate separation from Hikmatic

The commercial site, [hikmatic.dev](https://hikmatic.dev), uses a cream and navy/steel/sea palette, rounded Nunito typography, coastal imagery, and arch/lattice motifs. ArabicBASIC should have an independent identity:

- Do not reuse Hikmatic's cream, navy, steel-blue, or sea-teal palette.
- Do not reuse its coastal, arched-window, or lattice/arabesque visual motifs.
- Use the lilac, plum, pomegranate, lapis-violet, and citron palette above, with typography and code specimens doing most of the visual work.
- Do not treat the two sites as a shared brand. The connection is authorship, not product identity.

## GitHub Pages implementation plan

### Site files

When implementation begins, keep the static site isolated from the interpreter documentation:

```text
site/
  index.html
  styles.css
  assets/        # only if the finished design needs local imagery or fonts
```

Use plain static HTML and CSS unless implementation reveals a real need for a build step. Do not publish the whole `docs/` directory as the site: it contains project documentation and research artifacts, while Pages should serve only the editorial page.

### Deployment workflow

Add a separate workflow, such as `.github/workflows/pages.yml`, rather than mixing Pages deployment into the existing CI or release workflow.

The workflow should:

1. Deploy on pushes to the default branch (`main`) and support manual `workflow_dispatch` runs.
2. Check out the repository and configure GitHub Pages.
3. Upload only `site/` as the Pages artifact.
4. Deploy the artifact to the `github-pages` environment.
5. Use least-privilege permissions: `contents: read`, `pages: write`, and `id-token: write` where required by the deployment action.
6. Restrict deployment to the default branch through the environment/repository configuration. Pull requests may run validation, but must not publish the site.

In the repository's **Settings → Pages**, choose **GitHub Actions** as the publishing source. The project-site URL is expected to be:

```text
https://mgatto.github.io/ArabicBASIC/
```

Use relative asset paths so the site works under the `/ArabicBASIC/` project path. The Pages artifact must contain the built static site, not the repository root.

### Verification checklist

- [ ] Confirm the published page contains only the intended editorial site, not repository `docs/` contents.
- [ ] Confirm every outbound project link points to the repository `README.md`; there are no release, docs, demo, or social links.
- [ ] Check the project-page path and all relative assets at the published `/ArabicBASIC/` URL.
- [ ] Review the page at narrow mobile and desktop widths.
- [ ] Check keyboard access, visible focus, heading structure, and color contrast.
- [ ] Check that Arabic display text and code are selectable, copyable, and rendered in the intended direction without changing the program's logical order.
- [ ] Confirm the English copy remains editable as text and is organized for a future professional Arabic translation.
- [ ] Confirm that present-day capabilities and future intentions are clearly distinguished.

## Source notes

Use these as internal grounding when drafting and implementing the site:

- [`README.md`](../README.md) — current project description, design principles, Arabic phrase, interpreter and desktop experience.
- [`docs/tutorial-en.md`](tutorial-en.md) — current Arabic syntax and examples.
- [`docs/future/rtl-repl-options.md`](future/rtl-repl-options.md) — rationale and current status of the RTL-friendly editor and desktop session.
- [`docs/thesis/thesis-style-proposal-plan.md`](thesis/thesis-style-proposal-plan.md) — thesis framing around interpreter engineering, corpus-informed keyword design, and sociolinguistic constraints.
- [`docs/Multilingual Programming with ArabicBASIC.pdf`](Multilingual%20Programming%20with%20ArabicBASIC.pdf) — earlier presentation of the multilingual-language-platform aspiration; treat its implementation plans as historical, not as current status.
- [`docs/boolean-style-guide.md`](boolean-style-guide.md), [`docs/while-keyword-history.md`](while-keyword-history.md), and the keyword/design notes — examples of deliberate language-design choices.
- Private Obsidian notes about the project’s origins, design choices, corpus research, and research assertion informed the editorial framing. Synthesize those ideas; do not copy private notes or vault links into the published page.

The iCloud Obsidian ArabicBASIC note is only a short task stub and does not establish a separate public direction.

## Documentation references for implementation

The GitHub Pages documentation reviewed for this plan recommends either a branch source for simple sites or a custom Actions workflow when selecting a specific build/publish artifact. Because this site must publish only `site/` and not the repository's `docs/` folder, use the custom Actions workflow approach.

- [GitHub Pages: configuring a publishing source](https://docs.github.com/en/pages/getting-started-with-github-pages/configuring-a-publishing-source-for-your-github-pages-site)
- [GitHub Pages: using custom workflows](https://docs.github.com/en/pages/getting-started-with-github-pages/using-custom-workflows-with-github-pages)
- [GitHub Pages: project-site locations](https://docs.github.com/en/pages/getting-started-with-github-pages/what-is-github-pages#types-of-github-pages-sites)
