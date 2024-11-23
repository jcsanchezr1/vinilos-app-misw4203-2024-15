package com.example.vinilos.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.ActivityCollectorDetailBinding
import com.example.vinilos.ui.adapters.CommentAdapter
import com.example.vinilos.ui.adapters.PerformerAdapter
import com.example.vinilos.ui.viewmodels.CollectorViewModel

class CollectorDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectorDetailBinding
    private lateinit var collectorViewModel: CollectorViewModel
    private lateinit var artistAdapter: PerformerAdapter
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCollectorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        artistAdapter = PerformerAdapter()
        commentAdapter = CommentAdapter()

        collectorViewModel = ViewModelProvider(
            this,
            CollectorViewModel.Factory(application)
        )[CollectorViewModel::class.java]

        binding.viewModel = collectorViewModel

        val collectorId = intent.getIntExtra(Constant.COLLECTOR_ID, -1)
        observeCollectorData(collectorId)

        binding.rvArtistList.apply {
            layoutManager = LinearLayoutManager(this@CollectorDetailActivity)
            adapter = artistAdapter
        }

        binding.rvCommentsList.apply {
            layoutManager = LinearLayoutManager(this@CollectorDetailActivity)
            adapter = commentAdapter
        }

        binding.btnCollectorDetailBack.setOnClickListener {
            finish()
        }

    }

    private fun observeCollectorData(collectorId: Int) {
        collectorViewModel.getCollectorById(collectorId).observe(this) { collector ->
            if (collector != null) {
                binding.collector = collector

                artistAdapter.submitList(collector.favoritePerformers)
                commentAdapter.submitList(collector.comments)

            }
        }
    }

}