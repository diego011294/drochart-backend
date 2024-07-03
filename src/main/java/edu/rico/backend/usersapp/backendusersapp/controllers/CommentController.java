package edu.rico.backend.usersapp.backendusersapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.rico.backend.usersapp.backendusersapp.models.entities.Comment;
import edu.rico.backend.usersapp.backendusersapp.services.CommentService;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin( originPatterns = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create/{imageId}")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment,
                                                @PathVariable Long imageId) {
        Comment createdComment = commentService.createComment(comment, imageId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
    
    @GetMapping("/image/{imageId}")
    public ResponseEntity<Page<Comment>> getCommentsByImageId(
            @PathVariable Long imageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentService.getCommentsByImageId(imageId, pageRequest);
        return ResponseEntity.ok(commentsPage);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}