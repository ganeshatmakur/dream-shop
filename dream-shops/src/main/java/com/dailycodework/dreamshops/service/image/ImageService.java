package com.dailycodework.dreamshops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dailycodework.dreamshops.dto.ImageDto;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Image;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ImageRepository;
import com.dailycodework.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
	private final ImageRepository imageRepository;
	private final IProductService iProductService;

	@Override
	public Image getImageById(Long id) {
		return imageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No image are Found With this Id: " + id));
	}

	@Override
	public void deleteImageById(long id) {
		imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
			throw new ResourceNotFoundException("No Image are Found with this id: " + id);
		});
	}

	@Override
	public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
		Product product = iProductService.getProductById(productId);
		List<ImageDto> savedimageDtos = new ArrayList<>();

		for (MultipartFile file : files) {

			try {
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImages(new SerialBlob(file.getBytes()));
				image.setProduct(product);

				String buildDownloadUrl = "/api/v1/images/image/download/";
				String downloadUrl = buildDownloadUrl + image.getId();
				image.setDownloadurl(downloadUrl);
				Image savedImage = imageRepository.save(image);

				savedImage.setDownloadurl(buildDownloadUrl + savedImage.getId());
				imageRepository.save(savedImage);

				ImageDto imageDto = new ImageDto();
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadurl());
				savedimageDtos.add(imageDto); 

			} catch (IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}

		}
		return savedimageDtos;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = getImageById(imageId);
		try {
			image.setFileName(file.getOriginalFilename());
			image.setImages(new SerialBlob(file.getBytes()));
			imageRepository.save(image);
		} catch (IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
