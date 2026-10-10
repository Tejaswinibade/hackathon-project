function recommend(category) {
    const result = document.getElementById("result");
    const movieGrid = document.getElementById("movie-grid");

    const recommendations = {
        action: {
            headline: "🔥 Action mode: buckle up for adrenaline-pumping picks!",
            movies: [
                {
                    emoji: "🚗",
                    title: "Mad Max: Fury Road",
                    meta: "Action • 2015",
                    desc: "A relentless desert chase with explosive action and unforgettable visuals."
                },
                {
                    emoji: "🥷",
                    title: "John Wick",
                    meta: "Action • 2014",
                    desc: "A stylish revenge thriller packed with precision, tension, and intense combat."
                },
                {
                    emoji: "🦸",
                    title: "The Dark Knight",
                    meta: "Action • 2008",
                    desc: "Dark, gripping, and brilliantly layered with high-stakes heroism."
                }
            ]
        },
        comedy: {
            headline: "😂 Comedy time: pick something easy, funny, and light-hearted!",
            movies: [
                {
                    emoji: "😄",
                    title: "Superbad",
                    meta: "Comedy • 2007",
                    desc: "Wild, irreverent, and packed with hilarious chaos from start to finish."
                },
                {
                    emoji: "🎉",
                    title: "The Hangover",
                    meta: "Comedy • 2009",
                    desc: "A ridiculous bachelor-party adventure with nonstop laughs and surprises."
                },
                {
                    emoji: "😁",
                    title: "Bridesmaids",
                    meta: "Comedy • 2011",
                    desc: "Smart, funny, and full of chaotic friendship energy and memorable scenes."
                }
            ]
        },
        family: {
            headline: "👨‍👩‍👧‍👦 Family night: warm, colorful, and fun for everyone!",
            movies: [
                {
                    emoji: "🧸",
                    title: "Toy Story",
                    meta: "Family • 1995",
                    desc: "A timeless adventure about friendship, courage, and imagination."
                },
                {
                    emoji: "🦁",
                    title: "The Lion King",
                    meta: "Family • 1994",
                    desc: "Epic, emotional, and beautifully told with unforgettable songs and moments."
                },
                {
                    emoji: "🌈",
                    title: "Encanto",
                    meta: "Family • 2021",
                    desc: "Vibrant, joyful, and full of heart, music, and family celebration."
                }
            ]
        },
        trending: {
            headline: "⭐ Trending picks: what everyone is talking about right now!",
            movies: [
                {
                    emoji: "🌌",
                    title: "Dune",
                    meta: "Sci‑Fi • 2021",
                    desc: "Immersive world-building and cinematic scale make this a modern blockbuster."
                },
                {
                    emoji: "🎀",
                    title: "Barbie",
                    meta: "Comedy • 2023",
                    desc: "Colorful, clever, and playful with a strong pop-culture punch."
                },
                {
                    emoji: "⚡",
                    title: "Oppenheimer",
                    meta: "Drama • 2023",
                    desc: "Powerful, intense, and deeply thought-provoking with huge storytelling impact."
                }
            ]
        }
    };

    if (!result || !recommendations[category]) {
        return;
    }

    const selected = recommendations[category];
    result.innerText = selected.headline;

    if (movieGrid) {
        movieGrid.innerHTML = selected.movies.map(movie => `
            <article class="movie-card">
                <div class="movie-poster">${movie.emoji}</div>
                <div class="movie-body">
                    <h3 class="movie-title">${movie.title}</h3>
                    <div class="movie-meta">${movie.meta}</div>
                    <p class="movie-desc">${movie.desc}</p>
                </div>
            </article>
        `).join('');
    }
}
