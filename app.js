function recommend(category) {

    const result = document.getElementById("result");

    const recommendations = {

        action: "🔥 Try an exciting action movie tonight!",

        comedy: "😂 How about a comedy for a fun movie night?",

        family: "👨‍👩‍👧‍👦 Here are some family-friendly choices for everyone.",

        trending: "⭐ Check out the latest trending shows and movies!"
    };

    result.innerText = recommendations[category];
}