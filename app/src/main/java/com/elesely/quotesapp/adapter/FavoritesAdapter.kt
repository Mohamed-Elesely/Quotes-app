import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elesely.quotesapp.R
import com.elesely.quotesapp.db.FavoriteQuotes

class FavoritesAdapter(var favoriteQuotes: List<FavoriteQuotes>) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_quote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = favoriteQuotes[position]
        holder.textViewContent.text = quote.content
        holder.textViewAuthor.text = quote.author
    }

    override fun getItemCount(): Int {
        return favoriteQuotes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)
        val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
    }
}
